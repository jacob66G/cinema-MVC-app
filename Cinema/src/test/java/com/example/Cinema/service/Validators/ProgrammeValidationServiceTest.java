package com.example.Cinema.service.Validators;

import com.example.Cinema.model.CinemaHall;
import com.example.Cinema.model.Dto.ProgrammeDto;
import com.example.Cinema.model.Movie;
import com.example.Cinema.model.Programme;
import com.example.Cinema.repository.MovieRepository;
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
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

class ProgrammeValidationServiceTest {

    @Mock
    ProgrammeRepository programmeRepository;

    @Mock
    MovieRepository movieRepository;

    @InjectMocks
    ProgrammeValidationService underTest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testIsCinemaHallAvailable_whenCinemaHallIsNotAvailable() {
        //given
        LocalDate date = LocalDate.of(2020, 1, 1);
        LocalTime time = LocalTime.of(12, 0);

        Movie movie = new Movie("Test", "Test", 120);

        CinemaHall cinemaHall = new CinemaHall("A");

        ProgrammeDto programmeDto = new ProgrammeDto();
        programmeDto.setCinemaHallName(cinemaHall.getName());
        programmeDto.setDate(date);
        programmeDto.setTime(time);
        programmeDto.setIdmovie(1L);
        programmeDto.setId(1L);

        Programme existingProgramme = new Programme();
        existingProgramme.setDate(date);
        existingProgramme.setTime(time);
        existingProgramme.setMovie(movie);
        existingProgramme.setCinemaHall(cinemaHall);

        List<Programme> conflictingProgrammes = Arrays.asList(existingProgramme);

        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));
        when(programmeRepository.findConflictingProgrammes(cinemaHall.getName(), date, 1L)).thenReturn(conflictingProgrammes);

        //when
        boolean isAvailable = underTest.isCinemaHallAvailable(programmeDto);

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
        programmeDto.setIdmovie(1L);

        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));
        when(programmeRepository.findConflictingProgrammes(cinemaHall.getName(), date, null)).thenReturn(List.of());

        //when
        boolean isAvailable = underTest.isCinemaHallAvailable(programmeDto);

        //then
        assertThat(isAvailable).isTrue();
    }
}
