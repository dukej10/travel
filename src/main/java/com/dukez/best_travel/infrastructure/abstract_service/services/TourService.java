package com.dukez.best_travel.infrastructure.abstract_service.services;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.dukez.best_travel.api.models.request.TourRequest;
import com.dukez.best_travel.api.models.response.TourResponse;
import com.dukez.best_travel.domain.entities.FlyEntity;
import com.dukez.best_travel.domain.entities.HotelEntity;
import com.dukez.best_travel.domain.entities.TourEntity;
import com.dukez.best_travel.domain.repositories.CustomerRepository;
import com.dukez.best_travel.domain.repositories.FlyRepository;
import com.dukez.best_travel.domain.repositories.HotelRepository;
import com.dukez.best_travel.domain.repositories.TourRepository;
import com.dukez.best_travel.infrastructure.abstract_service.ITourService;
import com.dukez.best_travel.infrastructure.helpers.CustomerHelper;
import com.dukez.best_travel.infrastructure.helpers.TourHelper;
import java.util.stream.Collectors;
import java.util.HashSet;
import java.util.HashMap;

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

    @Override
    public TourResponse create(TourRequest request) {
        var customer = this.customerRepository.findById(request.getCustomerId()).orElseThrow();
        var flights = new HashSet<FlyEntity>();
        request.getFlights().forEach(fly -> {
            var flyEntity = this.flyRepository.findById(fly.getId()).orElseThrow();
            flights.add(flyEntity);
        });
        var hotels = new HashMap<HotelEntity, Integer>();
        request.getHotels().forEach(hotel -> {
            var hotelEntity = this.hotelRepository.findById(hotel.getId()).orElseThrow();
            hotels.put(hotelEntity, hotel.getTotalDays());
        });
        var tourToSave = TourEntity.builder().tickets(this.tourHelper.createTickets(flights, customer))
                .reservations(this.tourHelper.createReservations(hotels, customer)).customer(customer).build();
        var tourSaved = this.tourRepository.save(tourToSave);
        // Incrementar el total de tours del cliente
        this.customerHelper.incrase(customer.getDni(), this.getClass());
        return TourResponse.builder()
                .reservationsIds(tourSaved
                        .getReservations().stream().map(reservation -> reservation.getId()).collect(Collectors.toSet()))
                .ticketsIds(
                        tourSaved.getTickets().stream().map(ticket -> ticket.getId()).collect(Collectors.toSet()))
                .id(tourSaved.getId())
                .build();
    }

    @Override
    public TourResponse read(Long id) {
        var tourFromDB = this.tourRepository.findById(id).orElseThrow();
        return TourResponse.builder()
                .reservationsIds(tourFromDB
                        .getReservations().stream().map(reservation -> reservation.getId()).collect(Collectors.toSet()))
                .ticketsIds(
                        tourFromDB.getTickets().stream().map(ticket -> ticket.getId()).collect(Collectors.toSet()))
                .id(tourFromDB.getId()).build();
    }

    @Override
    public void delete(Long id) {
        var tourToDelete = this.tourRepository.findById(id).orElseThrow();
        this.tourRepository.delete(tourToDelete);
    }

    @Override
    public void removeTicket(UUID ticketId, Long tourId) {
        var tourUpdate = this.tourRepository.findById(tourId).orElseThrow();
        tourUpdate.removeTicket(ticketId);
        this.tourRepository.save(tourUpdate);
    }

    @Override
    public UUID addTicket(Long flyId, Long tourId) {
        var tourUpdate = this.tourRepository.findById(tourId).orElseThrow();

        var fly = this.flyRepository.findById(flyId).orElseThrow();
        var ticket = this.tourHelper.createTicket(fly, tourUpdate.getCustomer());
        tourUpdate.addTicket(ticket);
        this.tourRepository.save(tourUpdate);
        return ticket.getId();
    }

    @Override
    public void removeReservation(UUID reservationId, Long tourId) {
        var tourUpdate = this.tourRepository.findById(tourId).orElseThrow();
        tourUpdate.removeReservation(reservationId);
        this.tourRepository.save(tourUpdate);
    }

    @Override
    public UUID addReservation(Long hotelId, Long tourId, Integer totalDays) {
        var tourUpdate = this.tourRepository.findById(tourId).orElseThrow();
        var hotel = this.hotelRepository.findById(hotelId).orElseThrow();
        var reservation = this.tourHelper.createReservation(hotel, tourUpdate.getCustomer(), totalDays);
        tourUpdate.addReservation(reservation);
        this.tourRepository.save(tourUpdate);
        return reservation.getId();
    }

}
