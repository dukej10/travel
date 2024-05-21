package com.dukez.best_travel.domain.repositories.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.dukez.best_travel.domain.entities.jpa.FlyEntity;

import java.util.Set;
import java.math.BigDecimal;
import java.util.UUID;
import java.util.Optional;

public interface FlyRepository extends JpaRepository<FlyEntity, Long> {

    @Query("select f from fly f where f.price < :price")
    Set<FlyEntity> selectLessPrice(BigDecimal price);

    @Query("select f from fly f where f.price between :min and :max")
    Set<FlyEntity> selectBetweenPrice(BigDecimal min, BigDecimal max);

    @Query("select f from fly f where f.originName = :origin and f.destinyName = :destiny")
    Set<FlyEntity> selectOriginDestiny(String origin, String destiny);

    @Query("select f from fly f join fetch f.tickets t where t.id = :ticketId")
    Optional<FlyEntity> findByTicketId(UUID ticketId);
}