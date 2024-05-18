package com.dukez.best_travel.infrastructure.services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.dukez.best_travel.api.models.request.ReservationRequest;
import com.dukez.best_travel.api.models.response.HotelResponse;
import com.dukez.best_travel.api.models.response.ReservationResponse;
import com.dukez.best_travel.domain.entities.ReservationEntity;
import com.dukez.best_travel.domain.repositories.CustomerRepository;
import com.dukez.best_travel.domain.repositories.HotelRepository;
import com.dukez.best_travel.domain.repositories.ReservationRepository;
import com.dukez.best_travel.infrastructure.abstract_service.IReservationService;
import com.dukez.best_travel.infrastructure.helpers.ApiCurrencyConnectorHelper;
import com.dukez.best_travel.infrastructure.helpers.BlackListHelper;
import com.dukez.best_travel.infrastructure.helpers.CustomerHelper;
import com.dukez.best_travel.util.consts.Tables;
import com.dukez.best_travel.util.exceptions.IdNotFoundException;
import com.dukez.best_travel.util.functions.Functions;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Transactional
@Service
@AllArgsConstructor
@Slf4j
public class ReservationService implements IReservationService {

        private final ReservationRepository reservationRepository;
        private final HotelRepository hotelRepository;
        private final CustomerRepository customerRepository;
        private final CustomerHelper customerHelper;
        private final BlackListHelper blackListHelper;
        private final ApiCurrencyConnectorHelper currencyConnectorHelper;
        public static final BigDecimal charger_price_reservation = BigDecimal.valueOf(0.2);
        private final Functions functions;

        @Override
        public ReservationResponse create(ReservationRequest request) {
                // Verificar si el cliente está en la lista negra
                blackListHelper.isInBlackListCustomer(request.getIdClient());
                var hotel = hotelRepository.findById(request.getIdHotel())
                                .orElseThrow(() -> new IdNotFoundException(Tables.hotel.name()));
                var customer = customerRepository.findById(request.getIdClient())
                                .orElseThrow(() -> new IdNotFoundException(Tables.customer.name()));
                var reservationToPersist = ReservationEntity.builder().id(UUID.randomUUID()).customer(
                                customer).hotel(hotel)
                                .dateStart(
                                                LocalDate.now()

                                ).dateEnd(LocalDate.now().plusDays(request.getTotalDays()))
                                .dateTimeReservation(LocalDateTime.now())
                                .totalDays(request.getTotalDays())
                                .price(hotel.getPrice().add(hotel.getPrice().multiply(charger_price_reservation)))
                                .build();
                var reservation = reservationRepository.save(reservationToPersist);
                // Incrementar el número de reservas del cliente
                this.customerHelper.incrase(customer.getDni(), this.getClass());
                this.functions.sendEmail(request.getEmail(), customer.getFullName(), Tables.reservation.name());
                return entityToResponse(reservation);
        }

        @Override
        public ReservationResponse read(UUID id) {
                var reservationFromDB = reservationRepository.findById(id)
                                .orElseThrow(() -> new IdNotFoundException(Tables.reservation.name()));
                return entityToResponse(reservationFromDB);
        }

        @Override
        public BigDecimal findPrice(Long hotelId) {
                log.info("Finding price for fly with id {}", hotelId);
                var hotel = hotelRepository.findById(hotelId).orElseThrow(
                                () -> new IdNotFoundException(Tables.hotel.name()));
                System.out.println("Fly: " + hotel);
                return hotel.getPrice().add(
                                hotel.getPrice().multiply(charger_price_reservation));
        }

        @Override
        public ReservationResponse update(UUID id, ReservationRequest request) {
                var hotel = hotelRepository.findById(request.getIdHotel()).orElseThrow(
                                () -> new IdNotFoundException(Tables.hotel.name()));
                var reservationToUpdate = this.reservationRepository.findById(id).orElseThrow(
                                () -> new IdNotFoundException(Tables.reservation.name()));
                reservationToUpdate.setHotel(hotel);
                reservationToUpdate.setTotalDays(request.getTotalDays());
                reservationToUpdate
                                .setPrice(hotel.getPrice().add(hotel.getPrice().multiply(charger_price_reservation)));
                reservationToUpdate.setDateEnd(LocalDate.now().plusDays(request.getTotalDays()));
                reservationToUpdate.setDateTimeReservation(LocalDateTime.now());
                reservationToUpdate.setDateStart(LocalDate.now());
                var reservationUpdate = this.reservationRepository.save(reservationToUpdate);
                log.info("Reservation updated with id {}", reservationToUpdate.getId());

                return entityToResponse(reservationUpdate);
        }

        @Override
        public void delete(UUID id) {
                var reservationToDelete = reservationRepository.findById(id).orElseThrow(
                                () -> new IdNotFoundException(Tables.reservation.name()));
                this.reservationRepository.delete(reservationToDelete);
        }

        private ReservationResponse entityToResponse(ReservationEntity entity) {
                var response = new ReservationResponse();
                BeanUtils.copyProperties(entity, response);
                var hotelResponse = new HotelResponse();
                BeanUtils.copyProperties(entity.getHotel(), hotelResponse);
                response.setHotel(hotelResponse);

                return response;
        }

        @Override
        public Map<String, Object> findPriceCurrency(Long hotelId, Currency currency) {
                Map<String, Object> response = new HashMap<>();
                String currencyCode = currency.getCurrencyCode();
                var hotel = this.hotelRepository.findById(hotelId).orElseThrow(
                                () -> new IdNotFoundException(Tables.hotel.name()));
                BigDecimal price = hotel.getPrice();

                var priceInDollars = hotel.getPrice().add(
                                hotel.getPrice().multiply(charger_price_reservation));
                if (currencyCode.equals("USD")) {
                        price = priceInDollars;

                } else {
                        var currencyDTO = this.currencyConnectorHelper.getCurrency(currency);
                        log.info("API Currency in {}, response: {}", currencyDTO.getExchangeDate().toString(),
                                        currencyDTO.getRates());
                        price = priceInDollars.multiply(currencyDTO.getRates().get(currency));
                }
                response.put("currency", currency.getCurrencyCode());

                response.put("price", price);

                return response;
        }

}
