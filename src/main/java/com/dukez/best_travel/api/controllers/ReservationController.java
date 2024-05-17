package com.dukez.best_travel.api.controllers;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dukez.best_travel.api.models.request.ReservationRequest;
import com.dukez.best_travel.api.models.response.ErrorsResponse;
import com.dukez.best_travel.api.models.response.ReservationResponse;
import com.dukez.best_travel.infrastructure.abstract_service.IReservationService;
import com.dukez.best_travel.util.jsonStrings.JsonStringResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/reservation")
@Tag(name = "Reservation", description = "Endpoints for managing reservations in the system.")
public class ReservationController {
        private final IReservationService reservationService;

        @Operation(summary = "Create a reservation in the system.")
        @PostMapping(path = "create")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "400", description = "When the request have a field invalid", content = {
                                        @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorsResponse.class), examples = {
                                                        @ExampleObject(value = JsonStringResponse.ReservationResponseInvalid)
                                        })
                        }),
                        @ApiResponse(responseCode = "200", description = "", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReservationResponse.class), examples = {
                                        @ExampleObject(value = JsonStringResponse.ReservationResponseOk) }))
        })

        public ResponseEntity<ReservationResponse> createReservation(
                        @Valid @RequestBody ReservationRequest reservation) {
                return ResponseEntity.ok(reservationService.create(reservation));
        }

        @ApiResponses(value = {
                        @ApiResponse(responseCode = "400", description = "When the request have a field invalid", content = {
                                        @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorsResponse.class), examples = {
                                                        @ExampleObject(value = JsonStringResponse.ReservationResponseInvalid)
                                        })
                        }),
                        @ApiResponse(responseCode = "200", description = "When the application has fulfilled the validations and has been successfully processed", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReservationResponse.class), examples = {
                                        @ExampleObject(value = JsonStringResponse.ReservationResponseOk) })),
                        @ApiResponse(responseCode = "403", description = "When the request has an idClient that is in the blacklist", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReservationResponse.class), examples = {
                                        @ExampleObject(value = JsonStringResponse.ReservationResponseForbidden) }))
        })
        @Operation(summary = "Update the reservation information that corresponds to the id provided.")
        @PutMapping(path = "update/{id}")
        public ResponseEntity<ReservationResponse> updateReservation(@PathVariable UUID id,
                        @Valid @RequestBody ReservationRequest reservation) {
                return ResponseEntity.ok(reservationService.update(id, reservation));
        }

        @Operation(summary = "Return a reservation based in id provided.")
        @GetMapping(path = "{id}")
        public ResponseEntity<ReservationResponse> getReservatio(@PathVariable UUID id) {
                return ResponseEntity.ok(reservationService.read(id));
        }

        @Operation(summary = "Return a reservation price given a hotel id.")
        @GetMapping(path = "find-price")
        public ResponseEntity<Map<String, BigDecimal>> findPriceReservation(@RequestParam Long hotelId) {
                return ResponseEntity.ok(Map.of("price", reservationService.findPrice(hotelId)));

        }

        @Operation(summary = "Return a reservation price given a hotel id.")
        @GetMapping(path = "find-price-currency")
        public ResponseEntity<Map<String, Object>> findPriceReservationCurrency(@RequestParam Long hotelId,
                        @RequestHeader(required = false) Currency currency) {
                if (Objects.isNull(currency))
                        currency = Currency.getInstance("USD");
                return ResponseEntity.ok(
                                reservationService.findPriceCurrency(hotelId,
                                                currency));

        }
}