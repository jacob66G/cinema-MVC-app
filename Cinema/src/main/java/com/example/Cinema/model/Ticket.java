package com.example.Cinema.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class Ticket {

    @Id
    @GeneratedValue
    private Long idticket;

    @ManyToOne
    @JoinColumn(name = "idprogramme", nullable = false)
    private Programme programme;

    @ManyToOne
    @JoinColumn(name="idreservation", nullable = false)
    private Reservation reservation;
    private String ticketType;
    private String seat;
    private Double price;

    public Ticket(Programme programme, Reservation reservation, String seat, String ticketType, Double price) {
        this.programme = programme;
        this.reservation = reservation;
        this.ticketType = ticketType;
        this.seat = seat;
        this.price = price;
    }
}
