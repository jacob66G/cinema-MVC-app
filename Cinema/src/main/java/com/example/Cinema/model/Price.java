package com.example.Cinema.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Price {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idprice;
    private String type;
    private Double priceValue;

    public Price(String type, Double value) {
        this.type = type;
        this.priceValue = value;
    }

}
