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

import com.dukez.best_travel.api.models.response.HotelResponse;
import com.dukez.best_travel.infrastructure.abstract_service.IHotelService;
import com.dukez.best_travel.util.SortType;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/hotel")
@Tag(name = "Hotel", description = "Endpoints for the hotel catalog in the system.")
public class HotelContoller {
    private final IHotelService hotelService;

    @Operation(summary = "Return all hotels in the system according to the page number, quantity and type of order indicated..")
    @GetMapping(path = "all")
    public ResponseEntity<Page<HotelResponse>> getAllFlies(@RequestParam Integer page, @RequestParam Integer size,
            @RequestHeader(required = false) SortType sortType) {
        if (Objects.isNull(sortType))
            sortType = SortType.NONE;
        var response = this.hotelService.realAll(page, size, sortType);
        return response.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(response);
    }

    @Operation(summary = "Returns all hotels in the system whose price is less than the price provided.")
    @GetMapping(path = "less-price")
    public ResponseEntity<Set<HotelResponse>> getLessPrice(@RequestParam BigDecimal price) {
        var response = this.hotelService.readLessPrice(price);
        return response.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(response);
    }

    @Operation(summary = "Returns all flights in the system whose price is within the range provided.")
    @GetMapping(path = "between-price")
    public ResponseEntity<Set<HotelResponse>> getBetweenPrice(@RequestParam BigDecimal min,
            @RequestParam BigDecimal max) {
        var response = this.hotelService.readBetweenPrice(min, max);
        return response.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(response);
    }

    @Operation(summary = "Returns all hotels in the system whose rating is greater than the rating provided.")
    @GetMapping(path = "greater-than")
    public ResponseEntity<Set<HotelResponse>> getGreaterThan(@RequestParam Integer rating) {
        var response = this.hotelService.readGreaterThan(rating);
        return response.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(response);
    }

}