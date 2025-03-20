package com.example.Cinema.service.Validators;

import com.example.Cinema.model.dto.MovieDto;
import com.example.Cinema.model.Ticket;
import com.example.Cinema.repository.MovieRepository;
import com.example.Cinema.repository.TicketRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MovieValidationServiceTest {

    @Mock
    MovieRepository movieRepository;

    @Mock
    TicketRepository ticketRepository;

    @InjectMocks
    MovieValidationService underTest;


    @Test
    void isTitleValid_titleValid_returnsTrue() {
        //given
        MovieDto movieDto = new MovieDto();
        movieDto.setTitle("title");
        movieDto.setIdmovie(1L);

        when(movieRepository.existsByTitle("title", 1L)).thenReturn(false);

        //when
        boolean result = underTest.isTitleValid(movieDto);

        //then
        assertTrue(result);
    }

    @Test
    void isTitleValid_titleNoValid_returnsFalse() {
        //given
        MovieDto movieDto = new MovieDto();
        movieDto.setTitle("TEST");
        movieDto.setIdmovie(1L);

        when(movieRepository.existsByTitle("TEST", 1L)).thenReturn(true);

        //when
        boolean result = underTest.isTitleValid(movieDto);

        //then
        assertFalse(result);
    }

    @Test
    void isTitleValid_whenIdIsNull_returnsFalseIfTitleExists() {
        //given
        MovieDto movieDto = new MovieDto();
        movieDto.setTitle("TEST");
        movieDto.setIdmovie(null);

        when(movieRepository.existsByTitle("TEST", null)).thenReturn(true);

        //when
        boolean result = underTest.isTitleValid(movieDto);

        //then
        assertFalse(result);
    }

    @Test
    void isMovieCanBeEdit_whenTicketsExist_returnsFalse() {
        //given
        long movieId = 1L;

        when(ticketRepository.findAllByProgramme_Movie_Id(movieId)).thenReturn(List.of(new Ticket(), new Ticket()));

        //when
        boolean result = underTest.isMovieCanBeEdit(movieId);

        //then
        assertFalse(result);
    }

    @Test
    void isMovieCanBeEdit_whenNoTickets_returnsTrue() {
        //given
        long movieId = 1L;

        when(ticketRepository.findAllByProgramme_Movie_Id(movieId)).thenReturn(new ArrayList<>());

        //when
        boolean result = underTest.isMovieCanBeEdit(movieId);

        //then
        assertTrue(result);
    }

    @Test
    void isMovieCanBeEdit_whenIdIsNull_returnsTrue() {
        //given
        Long movieId = null;

        //when
        boolean result = underTest.isMovieCanBeEdit(movieId);

        //then
        assertTrue(result);
    }
}