package com.example.Cinema.model.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class SeatDto {

    private long idseat;
    private String row;
    private int number;
    private boolean booked;
    private boolean chosen;
}
