package com.dukez.best_travel.infrastructure.abstract_service;

import java.util.Set;

import com.dukez.best_travel.api.models.response.FlyResponse;

public interface IFlyService extends CatalogService<FlyResponse> {
    Set<FlyResponse> readByOriginDestiny(String origin, String destiny);
}
