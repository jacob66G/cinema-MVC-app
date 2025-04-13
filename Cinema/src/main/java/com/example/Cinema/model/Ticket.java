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
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="reservation_id")
    private Reservation reservation;

    @Enumerated(EnumType.STRING)
    private TicketCategory ticketType;
    private Double price;

    @ManyToOne
    @JoinColumn(name = "seat_id", nullable = false)
    private Seat seat;

    public Ticket(Seat seat, TicketCategory ticketType, Double price) {
        this.ticketType = ticketType;
        this.seat = seat;
        this.price = price;
    }
}
