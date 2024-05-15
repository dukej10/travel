package com.dukez.best_travel.api.models.request;

import java.io.Serializable;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class ReservationRequest implements Serializable {

    @Size(min = 18, max = 20, message = "The idClient must be between 18 and 20 characters")
    @NotBlank(message = "The idClient is required")
    private String idClient;

    @Positive(message = "The idHotel must be a positive number")
    @NotNull(message = "The idHotel is required")
    private Long idHotel;

    @Min(value = 1, message = "The totalDays must be greater than 0")
    @Max(value = 30, message = "The totalDays must be less than 30")
    @NotNull(message = "The totalDays is required")
    private Integer totalDays;

    @Email(message = "The email is not valid")
    @NotBlank(message = "The email is required")
    private String email;
}
