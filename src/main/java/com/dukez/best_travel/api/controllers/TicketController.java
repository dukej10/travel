package com.dukez.best_travel.api.controllers;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Currency;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dukez.best_travel.api.models.request.TicketRequest;
import com.dukez.best_travel.api.models.response.ErrorsResponse;
import com.dukez.best_travel.api.models.response.TicketResponse;
import com.dukez.best_travel.infrastructure.abstract_service.ITickerService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@AllArgsConstructor
@RequestMapping(path = "ticket")
@Slf4j
@Tag(name = "Ticket", description = "Endpoints for managing tickets in the system.")
public class TicketController {

    private final ITickerService ticketService;

    @ApiResponse(responseCode = "400", description = "When the request have a field invalid", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorsResponse.class))
    })
    @Operation(summary = "Save a ticket for a flight in the system.")
    @PostMapping(path = "create")
    public ResponseEntity<TicketResponse> createTicket(@Valid @RequestBody TicketRequest ticket) {
        log.info("Creating ticket with id {}", ticket);
        return ResponseEntity.ok(ticketService.create(ticket));
    }

    @Operation(summary = "Return a ticket based in id provided.")
    @GetMapping(path = "{id}")
    public ResponseEntity<TicketResponse> getTicket(@PathVariable UUID id) {
        log.info("Getting ticket with id {}", id);
        return ResponseEntity.ok(ticketService.read(id));
    }

    @ApiResponse(responseCode = "400", description = "When the request have a field invalid", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorsResponse.class))
    })
    @Operation(summary = "Update the ticket information that corresponds to the id provided.")
    @PutMapping(path = "update/{id}")
    public ResponseEntity<TicketResponse> updateTicket(@PathVariable UUID id,
            @Valid @RequestBody TicketRequest entity) {
        return ResponseEntity.ok(ticketService.update(id, entity));
    }

    @Operation(summary = "Delete the ticket that corresponds to the provided id.")
    @DeleteMapping(path = "delete/{id}")
    public ResponseEntity<Void> deleteTicket(@PathVariable UUID id) {
        log.info("Deleting ticket with id {}", id);
        ticketService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Returns the price of the ticket for the flight with the id provided.")
    @GetMapping(path = "find-price")
    public ResponseEntity<Map<String, BigDecimal>> findPriceTicket(@RequestParam Long flyId) {
        log.info("Getting price for fly with id {}", flyId);
        return ResponseEntity.ok(Collections.singletonMap("flyPrice", ticketService.findPrice(flyId)));
    }

    @Operation(summary = "Returns the price of the ticket for the flight with the id provided.")
    @GetMapping(path = "find-price-currency")
    public ResponseEntity<Map<String, Object>> findPriceTicketCurrency(@RequestParam Long flyId,
            @RequestHeader(required = false) Currency currency) {
        log.info("Getting price for fly with id {}", flyId);
        return ResponseEntity.ok(ticketService.findPriceCurrency(flyId, currency));
    }

}