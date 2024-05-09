package com.dukez.best_travel.api.controllers;

import org.hibernate.mapping.Collection;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dukez.best_travel.api.models.request.TicketRequest;
import com.dukez.best_travel.api.models.response.TicketResponse;
import com.dukez.best_travel.constants.ApiConstant;
import com.dukez.best_travel.infrastructure.abstract_service.ITickerService;
import java.util.UUID;
import java.util.Map;
import java.math.BigDecimal;
import java.util.Collections;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@AllArgsConstructor
@RequestMapping(path = "ticket")
@Slf4j
public class TicketController {

    private final ITickerService ticketService;

    @PostMapping(path = "create")
    public ResponseEntity<TicketResponse> createTicket(@RequestBody TicketRequest ticket) {
        log.info("Creating ticket with id {}", ticket);
        return ResponseEntity.ok(ticketService.create(ticket));
    }

    @GetMapping(path = "{id}")
    public ResponseEntity<TicketResponse> getTicket(@PathVariable UUID id) {
        log.info("Getting ticket with id {}", id);
        return ResponseEntity.ok(ticketService.read(id));
    }

    @PutMapping(path = "update/{id}")
    public ResponseEntity<TicketResponse> updateTicket(@PathVariable UUID id, @RequestBody TicketRequest entity) {
        return ResponseEntity.ok(ticketService.update(id, entity));
    }

    @DeleteMapping(path = "delete/{id}")
    public ResponseEntity<Void> deleteTicket(@PathVariable UUID id) {
        log.info("Deleting ticket with id {}", id);
        ticketService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(path = "find-price")
    public ResponseEntity<Map<String, BigDecimal>> findPriceTicket(@RequestParam Long flyId) {
        log.info("Getting price for fly with id {}", flyId);
        return ResponseEntity.ok(Collections.singletonMap("flyPrice", ticketService.findPrice(flyId)));
    }

}