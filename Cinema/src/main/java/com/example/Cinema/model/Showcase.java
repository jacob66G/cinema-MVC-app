package com.example.Cinema.model;

import jakarta.persistence.*;
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

    @Lob
    private byte[] imageData;

    public Showcase(String type, String title, byte[] imageData) {
        this.type = type;
        this.title = title;
        this.imageData = imageData;
    }

}
