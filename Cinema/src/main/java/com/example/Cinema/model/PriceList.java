package com.example.Cinema.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class PriceList {

    @Id
    @GeneratedValue
    private Long idpricelist;
    private String ticketType;
    private Boolean weekendDay;
    private Double price;

    public PriceList(String ticketType, Boolean weekendDay, Double price) {
        this.ticketType = ticketType;
        this.weekendDay = weekendDay;
        this.price = price;
    }
}
