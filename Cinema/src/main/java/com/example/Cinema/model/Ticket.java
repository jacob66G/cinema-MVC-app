package com.example.Cinema.model;

import com.example.Cinema.model.enums.TicketCategory;
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
    private Long id;

    @ManyToOne
    @JoinColumn(name = "programme_id", nullable = false)
    private Programme programme;

    @ManyToOne
    @JoinColumn(name="reservation_id", nullable = true)
    private Reservation reservation;

    @Enumerated(EnumType.STRING)
    private TicketCategory ticketType;
    private Double price;

    @ManyToOne
    @JoinColumn(name = "seat_id", nullable = false)
    private Seat seat;

    public Ticket(Programme programme, Reservation reservation, Seat seat, TicketCategory ticketType, Double price) {
        this.programme = programme;
        this.reservation = reservation;
        this.ticketType = ticketType;
        this.seat = seat;
        this.price = price;
    }
}
