package com.dukez.best_travel.api.controllers;

import com.dukez.best_travel.api.models.request.ReservationRequest;
import com.dukez.best_travel.api.models.request.TicketRequest;
import com.dukez.best_travel.api.models.response.ReservationResponse;
import com.dukez.best_travel.api.models.response.TicketResponse;
import com.dukez.best_travel.infrastructure.abstract_service.IReservationService;
import com.dukez.best_travel.infrastructure.abstract_service.ITickerService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@AllArgsConstructor
@RequestMapping("/reservation")
@Slf4j
public class ReservationController {
    private final ITickerService tickerService;
    private final IReservationService reservationService;

    @PostMapping(path = "create")
    public ResponseEntity<ReservationResponse> createTicket(@RequestBody ReservationRequest reservation) {
        return ResponseEntity.ok(reservationService.create(reservation));
    }

    @PutMapping(path = "update/{id}")
    public ResponseEntity<ReservationResponse> updateTicket(@PathVariable UUID id,
            @RequestBody ReservationRequest reservation) {
        return ResponseEntity.ok(reservationService.update(id, reservation));
    }

    @GetMapping(path = "{id}")
    public ResponseEntity<ReservationResponse> getReservatioEntity(@PathVariable UUID id) {
        return ResponseEntity.ok(reservationService.read(id));
    }

    @GetMapping(path = "find-price")
    public ResponseEntity<Map<String, BigDecimal>> findPriceTicket(@RequestParam Long hotelId) {
        return ResponseEntity.ok(Map.of("price", reservationService.findPrice(hotelId)));

    }
}