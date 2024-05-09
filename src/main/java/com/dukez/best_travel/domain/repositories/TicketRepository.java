package com.dukez.best_travel.domain.repositories;

import org.springframework.data.repository.CrudRepository;
import java.util.UUID;

import com.dukez.best_travel.domain.entities.TicketEntity;

public interface TicketRepository extends CrudRepository<TicketEntity, UUID> {

}
