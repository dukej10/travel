package com.dukez.best_travel.domain.entities;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Entity(name = "reservation")
@NoArgsConstructor // Contructor sin argumentos
@AllArgsConstructor // Contructor con todos los argumentos
@Data
@Builder
public class ReservationEntity implements Serializable {

    @Id
    private UUID id;

    @Column(name = "date_reservation")
    private LocalDateTime dateTimeReservation;

    @Column(name = "date_start")
    private LocalDate dateStart;

    @Column(name = "date_end")
    private LocalDate dateEnd;

    @ManyToOne
    @JoinColumn(name = "hotel_id")
    private HotelEntity hotel;

    @ManyToOne
    @JoinColumn(name = "tour_id")
    private TourEntity tour;

    private BigDecimal price;

    private Integer totalDays;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private CustomerEntity customer;

}
