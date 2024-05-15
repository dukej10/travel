package com.dukez.best_travel.api.models.request;

import java.io.Serializable;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class TourFlyRequest implements Serializable {
    @Positive(message = "The id fly must be a positive number")
    @NotBlank(message = "The id fly is required")
    public Long id;

}
