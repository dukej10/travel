package com.dukez.best_travel.infrastructure.abstract_service.services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.dukez.best_travel.api.models.request.ReservationRequest;
import com.dukez.best_travel.api.models.response.FlyResponse;
import com.dukez.best_travel.api.models.response.HotelResponse;
import com.dukez.best_travel.api.models.response.ReservationResponse;
import com.dukez.best_travel.api.models.response.TicketResponse;
import com.dukez.best_travel.domain.entities.ReservationEntity;
import com.dukez.best_travel.domain.entities.TicketEntity;
import com.dukez.best_travel.domain.repositories.CustomerRepository;
import com.dukez.best_travel.domain.repositories.HotelRepository;
import com.dukez.best_travel.domain.repositories.ReservationRepository;
import com.dukez.best_travel.infrastructure.abstract_service.IReservationService;

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

    private static final BigDecimal charger_price_reservation = BigDecimal.valueOf(0.2);

    @Override
    public ReservationResponse create(ReservationRequest request) {
        // TODO Auto-generated method stub
        var hotel = hotelRepository.findById(request.getIdHotel()).orElseThrow();
        var client = customerRepository.findById(request.getIdClient()).orElseThrow();
        var reservationToPersist = ReservationEntity.builder().id(UUID.randomUUID()).customer(client).hotel(hotel)
                .dateStart(
                        LocalDate.now()

                ).dateEnd(LocalDate.now().plusDays(request.getTotalDays())).dateTimeReservation(LocalDateTime.now())
                .totalDays(request.getTotalDays())
                .price(hotel.getPrice().add(hotel.getPrice().multiply(charger_price_reservation))).build();
        var reservation = reservationRepository.save(reservationToPersist);
        return entityToResponse(reservation);
    }

    @Override
    public ReservationResponse read(UUID id) {
        var entity = reservationRepository.findById(id).orElseThrow();
        return entityToResponse(entity);
    }

    @Override
    public BigDecimal findPrice(Long hotelId) {
        log.info("Finding price for fly with id {}", hotelId);
        // TODO Auto-generated method stub
        var hotel = hotelRepository.findById(hotelId).orElseThrow();
        System.out.println("Fly: " + hotel);
        return hotel.getPrice().add(
                hotel.getPrice().multiply(charger_price_reservation));
    }

    @Override
    public ReservationResponse update(UUID id, ReservationRequest request) {
        var hotel = hotelRepository.findById(request.getIdHotel()).orElseThrow();
        var reservationToUpdate = this.reservationRepository.findById(id).orElseThrow();
        reservationToUpdate.setHotel(hotel);
        reservationToUpdate.setTotalDays(request.getTotalDays());
        reservationToUpdate.setPrice(hotel.getPrice().add(hotel.getPrice().multiply(charger_price_reservation)));
        reservationToUpdate.setDateEnd(LocalDate.now().plusDays(request.getTotalDays()));
        reservationToUpdate.setDateTimeReservation(LocalDateTime.now());
        reservationToUpdate.setDateStart(LocalDate.now());
        var reservationUpdate = this.reservationRepository.save(reservationToUpdate);
        log.info("Reservation updated with id {}", reservationToUpdate.getId());

        return entityToResponse(reservationUpdate);
    }

    @Override
    public void delete(UUID id) {
        var reservationToDelete = reservationRepository.findById(id).orElseThrow();
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

}
