package com.dukez.best_travel.infrastructure.services;

import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.dukez.best_travel.api.models.request.TourRequest;
import com.dukez.best_travel.api.models.response.TourResponse;
import com.dukez.best_travel.domain.entities.jpa.FlyEntity;
import com.dukez.best_travel.domain.entities.jpa.HotelEntity;
import com.dukez.best_travel.domain.entities.jpa.TourEntity;
import com.dukez.best_travel.domain.repositories.jpa.CustomerRepository;
import com.dukez.best_travel.domain.repositories.jpa.FlyRepository;
import com.dukez.best_travel.domain.repositories.jpa.HotelRepository;
import com.dukez.best_travel.domain.repositories.jpa.TourRepository;
import com.dukez.best_travel.infrastructure.abstract_service.ITourService;
import com.dukez.best_travel.infrastructure.helpers.BlackListHelper;
import com.dukez.best_travel.infrastructure.helpers.CustomerHelper;
import com.dukez.best_travel.infrastructure.helpers.TourHelper;
import com.dukez.best_travel.util.consts.Tables;
import com.dukez.best_travel.util.exceptions.IdNotFoundException;
import com.dukez.best_travel.util.functions.Functions;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Transactional
@Service
@AllArgsConstructor

public class TourService implements ITourService {

        private final TourRepository tourRepository;
        private final CustomerRepository customerRepository;
        private final HotelRepository hotelRepository;
        private final FlyRepository flyRepository;
        private final TourHelper tourHelper;
        private final CustomerHelper customerHelper;
        private BlackListHelper blackListHelper;
        private final Functions functions;

        @Override
        public TourResponse create(TourRequest request) {
                // Verificar si el cliente está en la lista negra
                blackListHelper.isInBlackListCustomer(request.getCustomerId());

                var customer = this.customerRepository.findById(request.getCustomerId())
                                .orElseThrow(() -> new IdNotFoundException(Tables.customer.name()));
                var flights = new HashSet<FlyEntity>();
                request.getFlights().forEach(fly -> {
                        var flyEntity = this.flyRepository.findById(fly.getId()).orElseThrow(
                                        () -> new IdNotFoundException(Tables.fly.name()));
                        flights.add(flyEntity);
                });
                var hotels = new HashMap<HotelEntity, Integer>();
                request.getHotels().forEach(hotel -> {
                        var hotelEntity = this.hotelRepository.findById(hotel.getId())
                                        .orElseThrow(() -> new IdNotFoundException(Tables.hotel.name()));
                        hotels.put(hotelEntity, hotel.getTotalDays());
                });
                var tourToSave = TourEntity.builder().tickets(this.tourHelper.createTickets(flights, customer))
                                .reservations(this.tourHelper.createReservations(hotels, customer)).customer(customer)
                                .build();
                var tourSaved = this.tourRepository.save(tourToSave);
                // Incrementar el total de tours del cliente
                this.customerHelper.incrase(customer.getDni(), this.getClass());
                // Enviar correo electrónico al cliente para  confirmar la reserva
                this.functions.sendEmail(request.getEmail(), customer.getFullName(), Tables.reservation.name());
                return TourResponse.builder()
                                .reservationsIds(tourSaved
                                                .getReservations().stream().map(reservation -> reservation.getId())
                                                .collect(Collectors.toSet()))
                                .ticketsIds(
                                                tourSaved.getTickets().stream().map(ticket -> ticket.getId())
                                                                .collect(Collectors.toSet()))
                                .id(tourSaved.getId())
                                .build();
        }

        @Override
        public TourResponse read(Long id) {
                var tourFromDB = this.tourRepository.findById(id).orElseThrow(
                                () -> new IdNotFoundException(Tables.tour.name()));
                return TourResponse.builder()
                                .reservationsIds(tourFromDB
                                                .getReservations().stream().map(reservation -> reservation.getId())
                                                .collect(Collectors.toSet()))
                                .ticketsIds(
                                                tourFromDB.getTickets().stream().map(ticket -> ticket.getId())
                                                                .collect(Collectors.toSet()))
                                .id(tourFromDB.getId()).build();
        }

        @Override
        public void delete(Long id) {
                var tourToDelete = this.tourRepository.findById(id).orElseThrow(
                                () -> new IdNotFoundException(Tables.tour.name()));
                this.tourRepository.delete(tourToDelete);
        }

        @Override
        public void removeTicket(UUID ticketId, Long tourId) {
                var tourUpdate = this.tourRepository.findById(tourId).orElseThrow(
                                () -> new IdNotFoundException(Tables.tour.name()));
                tourUpdate.removeTicket(ticketId);
                this.tourRepository.save(tourUpdate);
        }

        @Override
        public UUID addTicket(Long flyId, Long tourId) {
                var tourUpdate = this.tourRepository.findById(tourId).orElseThrow(
                                () -> new IdNotFoundException(Tables.tour.name()));

                var fly = this.flyRepository.findById(flyId)
                                .orElseThrow(() -> new IdNotFoundException(Tables.fly.name()));
                var ticket = this.tourHelper.createTicket(fly, tourUpdate.getCustomer());
                tourUpdate.addTicket(ticket);
                this.tourRepository.save(tourUpdate);
                return ticket.getId();
        }

        @Override
        public void removeReservation(UUID reservationId, Long tourId) {
                var tourUpdate = this.tourRepository.findById(tourId).orElseThrow(
                                () -> new IdNotFoundException(Tables.tour.name()));
                tourUpdate.removeReservation(reservationId);
                this.tourRepository.save(tourUpdate);
        }

        @Override
        public UUID addReservation(Long hotelId, Long tourId, Integer totalDays) {
                var tourUpdate = this.tourRepository.findById(tourId).orElseThrow(
                                () -> new IdNotFoundException(Tables.tour.name()));
                var hotel = this.hotelRepository.findById(hotelId).orElseThrow(
                                () -> new IdNotFoundException(Tables.hotel.name()));
                var reservation = this.tourHelper.createReservation(hotel, tourUpdate.getCustomer(), totalDays);
                tourUpdate.addReservation(reservation);
                this.tourRepository.save(tourUpdate);
                return reservation.getId();
        }

}
