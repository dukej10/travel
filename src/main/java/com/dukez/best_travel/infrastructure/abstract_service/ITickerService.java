package com.dukez.best_travel.infrastructure.abstract_service;

import java.util.UUID;

import com.dukez.best_travel.api.models.request.TicketRequest;
import com.dukez.best_travel.api.models.response.TicketResponse;
import java.math.BigDecimal;

public interface ITickerService extends CrudService<TicketRequest, TicketResponse, UUID> {
    BigDecimal findPrice(Long flyId);
}
