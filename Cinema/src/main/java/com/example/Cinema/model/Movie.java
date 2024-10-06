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

public class Movie implements Comparable<Movie> {
    @Id
    @GeneratedValue
    private Long idmovie;

    private String title;
    private String description;
    private String imageAddress;
    private Integer duration;


    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL)
    private List<Programme> programmeList;

    public Movie(String title, String description, String imageAddress, Integer duration) {
        this.title = title;
        this.description = description;
        this.imageAddress = imageAddress;
        this.duration = duration;
    }

    @Override
    public int compareTo(Movie other) {
        return this.title.compareTo(other.title);
    }
}
