package com.dukez.best_travel.domain.repositories.jpa;

import org.springframework.data.repository.CrudRepository;

import com.dukez.best_travel.domain.entities.jpa.TourEntity;

public interface TourRepository extends CrudRepository<TourEntity, Long> {

}
