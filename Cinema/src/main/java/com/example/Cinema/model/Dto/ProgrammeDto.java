package com.example.Cinema.model.Dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProgrammeDto {
    private Long id;

    @NotNull(message = "nie wybrano filmu")
    private Long idmovie;

    @NotNull(message = "nie wybrano sali")
    private String cinemaHallName;

    @NotNull(message = "nie wybrano daty")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    @NotNull(message = "nie wybrano godziny")
    private LocalTime time;
}
