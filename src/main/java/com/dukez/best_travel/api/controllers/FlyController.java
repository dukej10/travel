package com.dukez.best_travel.api.controllers;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dukez.best_travel.api.models.response.FlyResponse;
import com.dukez.best_travel.infrastructure.abstract_service.IFlyService;
import com.dukez.best_travel.util.consts.SortType;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/fly")
@Tag(name = "Fly", description = "Endpoints for the flights catalog in the system.")
public class FlyController {

    private final IFlyService flyService;

    @Operation(summary = "Return all flights in the system according to the page number, quantity and type of order indicated..")
    @GetMapping(path = "all")
    public ResponseEntity<Page<FlyResponse>> getAllFlights(@RequestParam Integer page, @RequestParam Integer size,
            @RequestHeader(required = false) SortType sortType) {
        if (Objects.isNull(sortType))
            sortType = SortType.NONE;
        var response = this.flyService.realAll(page, size, sortType);
        return response.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(response);
    }

    @Operation(summary = "Returns all flights in the system whose price is less than the price provided.")
    @GetMapping(path = "less-price")
    public ResponseEntity<Set<FlyResponse>> getLessPrice(@RequestParam BigDecimal price) {
        var response = this.flyService.readLessPrice(price);
        return response.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(response);
    }

    @Operation(summary = "Returns all flights in the system whose price is within the range provided.")
    @GetMapping(path = "between-price")
    public ResponseEntity<Set<FlyResponse>> getBetweenPrice(@RequestParam BigDecimal min,
            @RequestParam BigDecimal max) {
        var response = this.flyService.readBetweenPrice(min, max);
        return response.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(response);
    }

    @Operation(summary = "Returns all flights in the system whose origin and destination are the same as those provided.")
    @GetMapping(path = "origin-destiny")
    public ResponseEntity<Set<FlyResponse>> getByOriginDestiny(@RequestParam String origin,
            @RequestParam String destiny) {
        var response = this.flyService.readByOriginDestiny(origin, destiny);
        return response.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(response);
    }

}
