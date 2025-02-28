package com.example.Cinema.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
@Entity
@ToString
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idticket;

    @ManyToOne
    @JoinColumn(name = "idprogramme", nullable = false)
    private Programme programme;

    @ManyToOne
    @JoinColumn(name="idreservation", nullable = false)
    private Reservation reservation;
    private String ticketType;
    private Double price;

    @ManyToOne
    @JoinColumn(name = "idseat", nullable = false)
    private Seat seat;

    public Ticket(Programme programme, Reservation reservation, Seat seat, String ticketType, Double price) {
        this.programme = programme;
        this.reservation = reservation;
        this.ticketType = ticketType;
        this.seat = seat;
        this.price = price;
    }
}
