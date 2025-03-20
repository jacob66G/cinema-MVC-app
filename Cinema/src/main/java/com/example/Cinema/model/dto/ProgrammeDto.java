package com.example.Cinema.model.dto;

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
    private Long idprogramme;

    @NotNull(message = "nie wybrano filmu")
    private Long idmovie;

    @NotNull(message = "nie wybrano sali")
    private String cinemaHallName;

    @NotNull(message = "nie wybrano daty")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    @NotNull(message = "nie wybrano godziny")
    private LocalTime time;

    private String movieDescription;

    private String movieTitle;

    private String movieBase64Image;

    public ProgrammeDto(Long idprogramme, LocalDate date, LocalTime time, String title, String base64Image) {
        this.idprogramme = idprogramme;
        this.date = date;
        this.time = time;
        this.movieTitle = title;
        this.movieBase64Image = base64Image;
    }
}
