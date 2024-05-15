package com.dukez.best_travel.api.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dukez.best_travel.api.models.request.TourRequest;
import com.dukez.best_travel.api.models.response.TourResponse;
import com.dukez.best_travel.infrastructure.abstract_service.ITourService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PatchMapping;
import java.util.UUID;
import java.util.Map;
import java.util.Collections;

@RestController
@AllArgsConstructor
@RequestMapping(path = "tour")
@Slf4j
public class TourController {

    private final ITourService tourService;

    @PostMapping(path = "create")
    public ResponseEntity<TourResponse> createTour(@Valid @RequestBody TourRequest request) {

        return ResponseEntity.ok(tourService.create(request));
    }

    @GetMapping(path = "{id}")
    public ResponseEntity<TourResponse> getTour(@PathVariable Long id) {
        return ResponseEntity.ok(tourService.read(id));
    }

    @DeleteMapping(path = "delete/{id}")
    public ResponseEntity<TourResponse> deleteTour(@PathVariable Long id) {
        this.tourService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping(path = "{tourId}/remove-ticket/{ticketId}")
    public ResponseEntity<TourResponse> deleteTicket(@PathVariable Long tourId, @PathVariable UUID ticketId) {
        this.tourService.removeTicket(ticketId, tourId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping(path = "{tourId}/add-ticket/{flyId}")
    public ResponseEntity<Map<String, UUID>> addTicket(@PathVariable Long flyId, @PathVariable Long tourId) {
        var response = Collections.singletonMap("ticketId", this.tourService.addTicket(flyId, tourId));

        return ResponseEntity.ok(response);
    }

    @PatchMapping(path = "{tourId}/remove-reservation/{reservationId}")
    public ResponseEntity<TourResponse> deleteReservation(@PathVariable Long tourId, @PathVariable UUID reservationId) {
        this.tourService.removeReservation(reservationId, tourId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping(path = "{tourId}/add-reservation/{hotelId}")
    public ResponseEntity<Map<String, UUID>> addReservation(@PathVariable Long hotelId, @PathVariable Long tourId,
            @RequestParam Integer totalDays) {
        var response = Collections.singletonMap("reservationId",
                this.tourService.addReservation(hotelId, tourId, totalDays));

        return ResponseEntity.ok(response);
    }

}
