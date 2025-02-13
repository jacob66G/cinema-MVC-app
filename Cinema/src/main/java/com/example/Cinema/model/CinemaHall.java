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
    private String name;

    public CinemaHall(String name) {
        this.name = name;
    }
}
