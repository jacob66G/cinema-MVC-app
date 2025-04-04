package com.example.Cinema.service.Validators;

import com.example.Cinema.exception.ValidationException;
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
    void shouldThrowException_WhenTitleAlreadyExists() {
        //given
        MovieDto movieDto = new MovieDto();
        movieDto.setTitle("title");
        movieDto.setIdmovie(1L);

        when(movieRepository.existsByTitle("title", 1L)).thenReturn(true);

        // when + then
        ValidationException ex = assertThrows(ValidationException.class, () ->
                underTest.validateTitleUniqueness("title", 1L));

        assertEquals("Film o tytule title już istnieje", ex.getMessage());
    }

    @Test
    void shouldDoNothing_WhenTitleIsUnique() {
        // given
        String title = "Nowy Film";
        Long currentMovieId = 2L;

        when(movieRepository.existsByTitle(title, currentMovieId)).thenReturn(false);

        // when + then
        assertDoesNotThrow(() -> underTest.validateTitleUniqueness(title, currentMovieId));
    }

    @Test
    void shouldThrowException_WhenMovieIsUsed() {
        //given
        MovieDto movieDto = new MovieDto();
        movieDto.setTitle("TEST");
        movieDto.setIdmovie(1L);


        when(ticketRepository.findAllByProgramme_Movie_Id(1L)).thenReturn(List.of(new Ticket()));

        //when + then
        ValidationException ex = assertThrows(ValidationException.class, () ->
                underTest.validateEditPermission(1L));

        assertEquals("Nie można zmodyfikować filmu: 1L ponieważ są do niej przypisane inne obiekty.", ex.getMessage());
    }

    @Test
    void shouldDoNothing_WhenMovieIsNotUsed() {
        //given
        MovieDto movieDto = new MovieDto();
        movieDto.setTitle("TEST");
        movieDto.setIdmovie(1L);

        when(ticketRepository.findAllByProgramme_Movie_Id(1L)).thenReturn(new ArrayList<>());

        //when + then
        assertDoesNotThrow(() -> underTest.validateEditPermission(1L));
    }
}