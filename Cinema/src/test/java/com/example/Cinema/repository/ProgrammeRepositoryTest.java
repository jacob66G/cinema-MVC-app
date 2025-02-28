package com.example.Cinema.repository;

import com.example.Cinema.model.CinemaHall;
import com.example.Cinema.model.Movie;
import com.example.Cinema.model.Programme;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ProgrammeRepositoryTest {

    @Autowired
    ProgrammeRepository underTest;

    @Autowired
    CinemaHallRepository cinemaHallRepository;

    @Autowired
    MovieRepository movieRepository;


    @AfterEach
    void tearDown() {
        underTest.deleteAll();
        cinemaHallRepository.deleteAll();
        movieRepository.deleteAll();
    }


    @Test
    void shouldFindConflictingProgrammes() {
        //given
        Movie movie = new Movie("test", "test", 100);
        movieRepository.save(movie);

        CinemaHall cinemaHall = new CinemaHall("A");
        cinemaHallRepository.save(cinemaHall);

        LocalDate date = LocalDate.of(2020, 1, 1);
        LocalTime time = LocalTime.of(20, 0);

        Programme programme1 = new Programme(movie, date, time, cinemaHall);
        Programme programme2 = new Programme(movie, date, time, cinemaHall);
        Programme programme3 = new Programme(movie, date, time, cinemaHall);

        underTest.save(programme1);
        Long id = underTest.findByDate(date).get(0).getIdprogramme();

        underTest.save(programme2);
        underTest.save(programme3);

        List<Programme> savedProgrammes = Arrays.asList(programme2, programme3);

        //when
        List<Programme> programmes = underTest.findConflictingProgrammes("A", date, id);
        programmes.forEach(p ->
                System.out.println(p.getIdprogramme()));

        //then
        assertThat(programmes).hasSameElementsAs(savedProgrammes);
    }

    @Test
    void shouldDoesNotFindConflictingProgrammes() {
        //given
        Movie movie = new Movie("test", "test", 100);
        movieRepository.save(movie);

        CinemaHall cinemaHall = new CinemaHall("A");
        cinemaHallRepository.save(cinemaHall);

        CinemaHall cinemaHall2 = new CinemaHall("B");
        cinemaHallRepository.save(cinemaHall2);

        LocalDate date = LocalDate.of(2020, 1, 1);
        LocalTime time = LocalTime.of(20, 0);

        Programme programme1 = new Programme(movie, date, time, cinemaHall);
        Programme programme2 = new Programme(movie, date.plusDays(1), time, cinemaHall);
        Programme programme3 = new Programme(movie, date, time, cinemaHall2);

        underTest.save(programme1);
        underTest.save(programme2);
        underTest.save(programme3);

        //when
        List<Programme> programmes = underTest.findConflictingProgrammes("A", date, 1L);

        //then
        assertThat(programmes).isEmpty();
    }
}