package com.example.Cinema.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class CinemaHall {

    @Id
    @GeneratedValue
    private Long idcinemaHall;
    private String name;
    private int numberOfRows;
    private int numberOfSeats;


    public CinemaHall(String name, int numberOfRows, int numberOfSeats) {
        this.name = name;
        this.numberOfRows = numberOfRows;
        this.numberOfSeats = numberOfSeats;
    }
}
