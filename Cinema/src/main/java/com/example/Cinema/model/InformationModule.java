package com.example.Cinema.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class InformationModule {
    @Id
    @GeneratedValue
    private Long idinformationModule;
    private String type;
    private String title;
    private String description;
    private String imageAddress;
    private String reviewAddress;

    public InformationModule(String type, String title, String description, String imageAddress, String reviewAddress) {
        this.type = type;
        this.title = title;
        this.description = description;
        this.imageAddress = imageAddress;
        this.reviewAddress = reviewAddress;
    }
}
