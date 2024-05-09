package com.dukez.best_travel.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dukez.best_travel.domain.entities.HotelEntity;
import java.util.Set;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

public interface HotelRepository extends JpaRepository<HotelEntity, Long> {
    Set<HotelEntity> findByPriceLessThan(BigDecimal price);

    Set<HotelEntity> findByPriceBetween(BigDecimal min, BigDecimal max);

    Set<HotelEntity> findByRatingGreaterThan(Integer rating);

    Optional<HotelEntity> findByReservationsId(UUID reservationId);
}
