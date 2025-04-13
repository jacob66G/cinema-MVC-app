package com.example.Cinema.model.dto;

import com.example.Cinema.model.enums.TicketCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TicketDto {

    private Long id;
    private SeatDto seat;
    private TicketCategory ticketType;
    private Double price;
}
