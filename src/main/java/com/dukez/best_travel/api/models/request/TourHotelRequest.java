package com.dukez.best_travel.api.models.request;

import java.io.Serializable;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class TourHotelRequest implements Serializable {
    @Positive(message = "The id fly must be a positive number")
    @NotBlank(message = "The id fly is required")
    public Long id;

    @Min(value = 1, message = "The totalDays must be greater than 0")
    @Max(value = 30, message = "The totalDays must be less than 30")
    @NotNull(message = "The totalDays is required")
    private Integer totalDays;
}