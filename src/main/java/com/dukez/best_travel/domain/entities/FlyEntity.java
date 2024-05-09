package com.dukez.best_travel.domain.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Set;

import com.dukez.best_travel.util.AeroLine;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity(name = "fly")
@NoArgsConstructor // Contructor sin argumentos
@AllArgsConstructor // Contructor con todos los argumentos
@Data
@Builder
public class FlyEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "origin_lat")
    private Double originLat;

    @Column(name = "origin_lng")
    private Double originLng;

    @Column(name = "destiny_lat")
    private Double destinyLat;

    @Column(name = "destiny_lng")
    private Double destinyLng;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "origin_name", length = 20)
    private String originName;

    @Column(name = "destiny_name", length = 20)
    private String destinyName;

    @Enumerated(EnumType.STRING)
    private AeroLine aeroLine;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "fly", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private Set<TicketEntity> tickets;

}