package com.example.Cinema.model.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@AllArgsConstructor
@Getter
@Setter
public class ProgrammeDto {
    private Long id;
    private LocalDate date;
    private LocalTime time;
    private String movieTitle;
    private String hallName;
}
