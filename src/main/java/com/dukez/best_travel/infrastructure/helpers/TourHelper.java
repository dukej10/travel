package com.dukez.best_travel.infrastructure.helpers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.dukez.best_travel.domain.entities.jpa.CustomerEntity;
import com.dukez.best_travel.domain.entities.jpa.FlyEntity;
import com.dukez.best_travel.domain.entities.jpa.HotelEntity;
import com.dukez.best_travel.domain.entities.jpa.ReservationEntity;
import com.dukez.best_travel.domain.entities.jpa.TicketEntity;
import com.dukez.best_travel.domain.repositories.jpa.ReservationRepository;
import com.dukez.best_travel.domain.repositories.jpa.TicketRepository;
import com.dukez.best_travel.infrastructure.services.ReservationService;
import com.dukez.best_travel.infrastructure.services.TicketService;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

/**
 * para separar las resposabilidades de creación de tickets y resevaciones para
 * tour
 */
@Transactional
@Component
@AllArgsConstructor
public class TourHelper {
        private final TicketRepository ticketRepository;
        private final ReservationRepository reservationRepository;

        /**
         * Crear un ticket por cada vuelo
         * 
         * @param flights
         * @param customer
         * @return
         */
        public Set<TicketEntity> createTickets(Set<FlyEntity> flights, CustomerEntity customer) {
                var response = new HashSet<TicketEntity>(flights.size());
                flights.forEach(fly -> {
                        var ticketToPersist = TicketEntity.builder()
                                        .id(UUID.randomUUID())
                                        .departureDate(LocalDateTime.now())
                                        .arrivalDate(LocalDateTime.now())
                                        .purchaseDate(LocalDate.now())
                                        .price(fly.getPrice().multiply(fly.getPrice().multiply(
                                                        TicketService.charger_price_percentage)))
                                        .fly(fly)
                                        .customer(customer)
                                        .build();
                        response.add(this.ticketRepository.save(ticketToPersist));
                });
                return response;
        }

        public Set<ReservationEntity> createReservations(HashMap<HotelEntity, Integer> hotels,
                        CustomerEntity customer) {
                var response = new HashSet<ReservationEntity>(hotels.size());
                hotels.forEach((hotel, totalDays) -> {
                        var reservationToPersist = ReservationEntity.builder().id(UUID.randomUUID()).customer(
                                        customer).hotel(hotel)
                                        .dateStart(
                                                        LocalDate.now()

                                        ).dateEnd(LocalDate.now().plusDays(
                                                        totalDays))
                                        .dateTimeReservation(LocalDateTime.now())
                                        .totalDays(
                                                        totalDays)
                                        .price(hotel.getPrice()
                                                        .add(hotel.getPrice().multiply(
                                                                        ReservationService.charger_price_reservation)))
                                        .build();
                        response.add(this.reservationRepository.save(reservationToPersist));
                });
                return response;
        }

        public TicketEntity createTicket(FlyEntity fly, CustomerEntity customer) {
                var ticketToPersist = TicketEntity.builder()
                                .id(UUID.randomUUID())
                                .departureDate(LocalDateTime.now())
                                .arrivalDate(LocalDateTime.now())
                                .purchaseDate(LocalDate.now())
                                .price(fly.getPrice().multiply(fly.getPrice().multiply(
                                                TicketService.charger_price_percentage)))
                                .fly(fly)
                                .customer(customer)
                                .build();
                return this.ticketRepository.save(ticketToPersist);
        }

        public ReservationEntity createReservation(HotelEntity hotel, CustomerEntity customer, Integer totalDays) {
                var reservationToPersist = ReservationEntity.builder().id(UUID.randomUUID()).customer(
                                customer).hotel(hotel)
                                .dateStart(
                                                LocalDate.now()

                                ).dateEnd(LocalDate.now().plusDays(
                                                totalDays))
                                .dateTimeReservation(LocalDateTime.now())
                                .totalDays(
                                                totalDays)
                                .price(hotel.getPrice()
                                                .add(hotel.getPrice().multiply(
                                                                ReservationService.charger_price_reservation)))
                                .build();
                return this.reservationRepository.save(reservationToPersist);
        }

}
