package com.dukez.best_travel.infrastructure.abstract_service;

import java.util.Set;

import com.dukez.best_travel.api.models.response.HotelResponse;

public interface IHotelService extends CatalogService<HotelResponse> {
    Set<HotelResponse> readGreaterThan(Integer rating);
}
