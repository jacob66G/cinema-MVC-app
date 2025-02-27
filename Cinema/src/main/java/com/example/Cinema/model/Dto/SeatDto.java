package com.example.Cinema.model.Dto;

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
