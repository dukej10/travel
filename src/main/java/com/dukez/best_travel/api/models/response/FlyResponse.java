package com.dukez.best_travel.api.models.response;

import java.io.Serializable;

import com.dukez.best_travel.util.AeroLine;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class FlyResponse implements Serializable {
    private Long id;

    private Double originLat;

    private Double originLng;

    private Double destinyLat;

    private Double destinyLng;

    private BigDecimal price;

    private String originName;

    private String destinyName;

    private AeroLine aeroLine;
}
