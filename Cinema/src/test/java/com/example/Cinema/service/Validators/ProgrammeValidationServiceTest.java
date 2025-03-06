package com.example.Cinema.service.Validators;

import com.example.Cinema.model.CinemaHall;
import com.example.Cinema.model.Dto.ProgrammeDto;
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

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
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
    void isCinemaHallAvailable_whenCinemaHallIsNotAvailable_returnFalse() {
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
    void isCinemaHallAvailable_whenCinemaHallIsAvailable_returnTrue() {
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

    @Test
    void isProgrammeCanBeEdit_whenTicketsExist_returnsFalse() {
        //given
        Long id = 1L;
        when(ticketRepository.findAllByProgramme_Idprogramme(id)).thenReturn(List.of(new Ticket(), new Ticket()));

        //when
        boolean result = underTest.isProgrammeCanBeEdit(id);

        //then
        assertThat(result).isFalse();
    }

    @Test
    void isProgrammeCanBeEdit_whenNoTickets_returnsTrue() {
        //given
        Long id = 1L;
        when(ticketRepository.findAllByProgramme_Idprogramme(id)).thenReturn(new ArrayList<>());

        //when
        boolean result = underTest.isProgrammeCanBeEdit(id);

        //then
        assertThat(result).isTrue();
    }

    @Test
    void isProgrammeCanBeEdit_whenIdIsNull_returnsTrue() {
        //given
        Long id = null;

        //when
        boolean result = underTest.isProgrammeCanBeEdit(id);

        //then
        assertThat(result).isTrue();
    }
}
