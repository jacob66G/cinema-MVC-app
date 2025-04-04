package com.example.Cinema.service.Validators;

import com.example.Cinema.exception.ValidationException;
import com.example.Cinema.model.CinemaHall;
import com.example.Cinema.model.dto.ProgrammeDto;
import com.example.Cinema.model.Movie;
import com.example.Cinema.model.Programme;
import com.example.Cinema.model.Ticket;
import com.example.Cinema.repository.MovieRepository;
import com.example.Cinema.repository.ProgrammeRepository;
import com.example.Cinema.repository.TicketRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;


import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProgrammeValidationServiceTest {

    @Mock
    ProgrammeRepository programmeRepository;

    @Mock
    MovieRepository movieRepository;

    @Mock
    TicketRepository ticketRepository;

    @InjectMocks
    ProgrammeValidationService underTest;

    @Test
    void shouldThrowException_whenCinemaHallIsNotAvailable() {
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
        programmeDto.setIdprogramme(1L);

        Programme existingProgramme = new Programme();
        existingProgramme.setDate(date);
        existingProgramme.setTime(time);
        existingProgramme.setMovie(movie);
        existingProgramme.setCinemaHall(cinemaHall);

        List<Programme> conflictingProgrammes = Arrays.asList(existingProgramme);

        LocalTime newProgrammeStartTime = programmeDto.getTime();
        LocalTime newProgrammeEndTime = programmeDto.getTime().plusMinutes(movie.getDuration() + 20);

        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));
        when(programmeRepository.findConflictingProgrammes(cinemaHall.getName(), date, 1L)).thenReturn(conflictingProgrammes);

        //when + then
        ValidationException ex = assertThrows(ValidationException.class, () -> underTest.validateCinemaHallAvailable(programmeDto));

        assertEquals("Sala kina jest zajeta w tym czasie: " + newProgrammeStartTime + " - " + newProgrammeEndTime, ex.getMessage());
    }

    @Test
    void shouldDoNothing_whenCinemaHallIsAvailable() {
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
        programmeDto.setIdprogramme(1L);

        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));
        when(programmeRepository.findConflictingProgrammes(cinemaHall.getName(), date, 1L)).thenReturn(new ArrayList<>());

        //when + then
        assertDoesNotThrow(() -> underTest.validateCinemaHallAvailable(programmeDto));
    }

    @Test
    void shouldThrowException_WhenProgrammeIsUsed() {
        //given
        Long id = 1L;
        when(ticketRepository.findAllByProgramme_Id(id)).thenReturn(List.of(new Ticket(), new Ticket()));

        //when + then
        ValidationException ex = assertThrows(ValidationException.class, ()-> underTest.validateEditPermission(id));

        assertEquals("Nie można zmodyfikować programu: "  +  id + " ponieważ są do niej przypisane inne obiekty.", ex.getMessage());
    }

    @Test
    void shouldDoNothing_WhenProgrammeIsNotUsed() {
        //given
        Long id = 1L;
        when(ticketRepository.findAllByProgramme_Id(id)).thenReturn(new ArrayList<>());

        //when + then
        assertDoesNotThrow(() -> underTest.validateEditPermission(id));
    }
}
