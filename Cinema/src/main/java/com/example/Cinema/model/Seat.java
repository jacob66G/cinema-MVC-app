package com.example.Cinema.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "seat_row")
    private String row;

    @Column(name = "seat_number")
    private int number;

    @ManyToOne
    @JoinColumn(name = "cinemahall_id")
    private CinemaHall cinemaHall;

    public Seat(String row, int number, CinemaHall cinemaHall) {
        this.row = row;
        this.number = number;
        this.cinemaHall = cinemaHall;
    }
}
