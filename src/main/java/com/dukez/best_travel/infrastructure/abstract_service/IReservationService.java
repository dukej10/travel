package com.dukez.best_travel.infrastructure.abstract_service;

import java.util.UUID;

import com.dukez.best_travel.api.models.request.ReservationRequest;
import com.dukez.best_travel.api.models.response.ReservationResponse;
import java.math.BigDecimal;

public interface IReservationService extends CrudService<ReservationRequest, ReservationResponse, UUID> {

    public BigDecimal findPrice(Long hotelId);
}
