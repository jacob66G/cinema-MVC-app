package com.example.Cinema.service.Validators;

import com.example.Cinema.model.Dto.MovieDto;
import com.example.Cinema.repository.MovieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class MovieValidationServiceTest {

    @Mock
    MovieRepository movieRepository;

    @InjectMocks
    MovieValidationService underTest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldTitleExist() {
        //given
        MovieDto movieDto = new MovieDto();
        movieDto.setTitle("TEST");
        movieDto.setIdmovie(1L);

        when(movieRepository.existsByTitle("TEST", 1L)).thenReturn(false);

        //when
        boolean result = underTest.isTitleValid(movieDto);

        //then
        assertTrue(result);
    }

    @Test
    void shouldTitleNotValid() {
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
}