package com.example.Cinema.model;

import com.example.Cinema.model.enums.TicketCategory;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@Getter
@Setter
@Entity
public class Price {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TicketCategory type;
    private Double priceValue;

    public Price(TicketCategory type, Double priceValue) {
        this.type = type;
        this.priceValue = priceValue;
    }
}
