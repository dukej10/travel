package com.dukez.best_travel.infrastructure.abstract_service;

import java.util.Currency;
import java.util.Map;
import java.util.UUID;

import com.dukez.best_travel.api.models.request.TicketRequest;
import com.dukez.best_travel.api.models.response.TicketResponse;
import java.math.BigDecimal;

public interface ITickerService extends CrudService<TicketRequest, TicketResponse, UUID> {
    public BigDecimal findPrice(Long flyId);

    public Map<String, Object> findPriceCurrency(Long flyId, Currency currency);
}
