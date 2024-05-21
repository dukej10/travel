package com.dukez.best_travel.domain.entities.jpa;

import java.io.Serializable;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreRemove;
import lombok.*;

import java.util.UUID;
import java.util.HashSet;
import java.util.Objects;

@Entity(name = "tour")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder

public class TourEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "tour", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private Set<ReservationEntity> reservations;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "tour", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private Set<TicketEntity> tickets;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne
    @JoinColumn(name = "id_customer")
    private CustomerEntity customer;

    /*
     * Este método se encarga de actualizar la fk de los tickets y las reservas
     * cuando se cree o se elimina un tour
     */
    @PrePersist
    @PreRemove
    public void updateFk() {
        this.tickets.forEach(ticket -> ticket.setTour(this));
        this.reservations.forEach(reservation -> reservation.setTour(this));
    }

    public void removeTicket(UUID id) {
        this.tickets.forEach(ticket -> {
            if (ticket.getId().equals(id)) {
                ticket.setTour(null); // eliminar referencia
            }
        });
    }

    public void addTicket(TicketEntity ticket) {
        if (Objects.isNull(this.tickets)) {
            this.tickets = new HashSet<>();

        }
        this.tickets.add(ticket);
        this.tickets.forEach(t -> t.setTour(this));
    }

    public void removeReservation(UUID id) {
        this.reservations.forEach(reservation -> {
            if (reservation.getId().equals(id)) {
                reservation.setTour(null); // eliminar referencia
            }
        });
    }

    public void addReservation(ReservationEntity reservation) {
        if (Objects.isNull(this.reservations)) {
            this.reservations = new HashSet<>();
        }
        this.reservations.add(reservation);
        this.reservations.forEach(r -> r.setTour(this));
    }

}
