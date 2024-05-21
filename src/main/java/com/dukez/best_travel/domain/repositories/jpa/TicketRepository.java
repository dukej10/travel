package com.dukez.best_travel.domain.repositories.jpa;

import org.springframework.data.repository.CrudRepository;

import com.dukez.best_travel.domain.entities.jpa.TicketEntity;

import java.util.UUID;

public interface TicketRepository extends CrudRepository<TicketEntity, UUID> {

}
