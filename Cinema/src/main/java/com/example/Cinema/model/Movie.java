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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idmovie;
    private String title;
    private String description;
    private Integer duration;

    @Lob
    private byte[] imageData;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Programme> programmeList;

    public Movie(String title, String description, byte[] imageData, Integer duration) {
        this.title = title;
        this.description = description;
        this.imageData = imageData;
        this.duration = duration;
    }

    public Movie(String title, String description, Integer duration) {
        this.title = title;
        this.description = description;
        this.duration = duration;
    }

    @Override
    public int compareTo(Movie other) {
        return this.title.compareTo(other.title);
    }
}
