package com.dukez.best_travel.api.controllers;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dukez.best_travel.api.models.request.ReservationRequest;
import com.dukez.best_travel.api.models.response.ReservationResponse;
import com.dukez.best_travel.infrastructure.abstract_service.IReservationService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/reservation")
public class ReservationController {
    private final IReservationService reservationService;

    @PostMapping(path = "create")
    public ResponseEntity<ReservationResponse> createReservation(@Valid @RequestBody ReservationRequest reservation) {
        return ResponseEntity.ok(reservationService.create(reservation));
    }

    @PutMapping(path = "update/{id}")
    public ResponseEntity<ReservationResponse> updateReservation(@PathVariable UUID id,
            @Valid @RequestBody ReservationRequest reservation) {
        return ResponseEntity.ok(reservationService.update(id, reservation));
    }

    @GetMapping(path = "{id}")
    public ResponseEntity<ReservationResponse> getReservatio(@PathVariable UUID id) {
        return ResponseEntity.ok(reservationService.read(id));
    }

    @GetMapping(path = "find-price")
    public ResponseEntity<Map<String, BigDecimal>> findPriceReservation(@RequestParam Long hotelId) {
        return ResponseEntity.ok(Map.of("price", reservationService.findPrice(hotelId)));

    }
}