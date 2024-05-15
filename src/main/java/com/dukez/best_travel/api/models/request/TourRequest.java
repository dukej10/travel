package com.dukez.best_travel.api.models.request;

import java.io.Serializable;
import java.util.Set;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class TourRequest implements Serializable {

    @Size(min = 18, max = 20, message = "The idClient must be between 18 and 20 characters")
    @NotBlank(message = "The customerId is required")
    public String customerId;

    @Size(min = 1, message = "The flights must have at least one flight")
    private Set<TourFlyRequest> flights;

    @Size(min = 1, message = "The hotels must have at least one hotel")
    private Set<TourHotelRequest> hotels;
}
