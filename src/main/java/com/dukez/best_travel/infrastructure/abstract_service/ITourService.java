package com.dukez.best_travel.infrastructure.abstract_service;

import java.util.UUID;

import com.dukez.best_travel.api.models.request.TourRequest;
import com.dukez.best_travel.api.models.response.TourResponse;

public interface ITourService extends SimpleCrudService<TourRequest, TourResponse, Long> {
    void removeTicket(UUID ticketId, Long tourId);

    UUID addTicket(Long flyId, Long tourId);

    void removeReservation(UUID reservationId, Long tourId);

    UUID addReservation(Long hotelId, Long tourId, Integer totalDays);
}
