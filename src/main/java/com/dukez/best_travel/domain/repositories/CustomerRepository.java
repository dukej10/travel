package com.dukez.best_travel.domain.repositories;

import org.springframework.data.repository.CrudRepository;

import com.dukez.best_travel.domain.entities.CustomerEntity;

public interface CustomerRepository extends CrudRepository<CustomerEntity, String> {
}
