package com.example.Cinema.service;

import com.example.Cinema.model.CinemaHall;
import com.example.Cinema.model.Dto.ProgrammeDto;
import com.example.Cinema.model.Movie;
import com.example.Cinema.model.Programme;
import com.example.Cinema.repository.ProgrammeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class ProgrammeServiceTest {


    @Mock
    ProgrammeRepository programmeRepository;

    @InjectMocks
    ProgrammeService programmeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnFalseWhenConflictingProgrammesExist() {
        //given
        LocalDate date = LocalDate.of(2020, 1, 1);
        LocalTime time = LocalTime.of(12, 0);

        Movie movie = new Movie("Test", "Test", 120);
        CinemaHall cinemaHall = new CinemaHall("A");

        ProgrammeDto programmeDto = new ProgrammeDto();
        programmeDto.setCinemaHallName(cinemaHall.getName());
        programmeDto.setDate(date);
        programmeDto.setTime(time);

        Programme existingProgramme = new Programme();
        existingProgramme.setTime(time);
        existingProgramme.setMovie(new Movie("test", "test", 100));
        existingProgramme.setCinemaHall(cinemaHall);

        List<Programme> conflictingProgrammes = Arrays.asList(existingProgramme);

        when(programmeRepository.findConflictingProgrammes(cinemaHall.getName(), date, null)).thenReturn(conflictingProgrammes);

        //when
        boolean isAvailable = programmeService.isCinemaHallAvailable(programmeDto, movie);

        //then
        assertThat(isAvailable).isFalse();
    }

    @Test
    void shouldReturnTrueWhenConflictingProgrammesExist() {
        //given
        LocalDate date = LocalDate.of(2020, 1, 1);
        LocalTime time = LocalTime.of(12, 0);

        Movie movie = new Movie("Test", "Test", 120);
        CinemaHall cinemaHall = new CinemaHall("A");

        ProgrammeDto programmeDto = new ProgrammeDto();
        programmeDto.setCinemaHallName(cinemaHall.getName());
        programmeDto.setDate(date);
        programmeDto.setTime(time);

        when(programmeRepository.findConflictingProgrammes(cinemaHall.getName(), date, null)).thenReturn(List.of());

        //when
        boolean isAvailable = programmeService.isCinemaHallAvailable(programmeDto, movie);

        //then
        assertThat(isAvailable).isTrue();
    }
}