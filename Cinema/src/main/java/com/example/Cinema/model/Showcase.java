package com.example.Cinema.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@NoArgsConstructor
@Getter
@Setter
@Entity
@ToString
public class Showcase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idShowcase;
    private String type;
    private String title;
    private String imageAddress;

    public Showcase(String type, String title, String imageAddress) {
        this.type = type;
        this.title = title;
        this.imageAddress = imageAddress;
    }

}
