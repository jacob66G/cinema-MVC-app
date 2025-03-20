package com.example.Cinema.repository;

import com.example.Cinema.model.CinemaHall;
import com.example.Cinema.model.Movie;
import com.example.Cinema.model.Programme;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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

    Movie movie;
    CinemaHall cinemaHallA;
    CinemaHall cinemaHallB;
    LocalDate date;
    LocalDate otherDate;
    LocalTime time;
    LocalTime otherTime;

    @BeforeEach
    void setUp() {
        movie = movieRepository.save(new Movie("test", "test", 100));
        cinemaHallA = cinemaHallRepository.save(new CinemaHall("A"));
        cinemaHallB = cinemaHallRepository.save(new CinemaHall("B"));
        date = LocalDate.of(2020, 1, 1);
        otherDate = LocalDate.of(2020, 1, 10);
        time = LocalTime.of(10, 0);
        otherTime = LocalTime.of(20, 0);
    }


    @AfterEach
    void tearDown() {
        underTest.deleteAll();
        cinemaHallRepository.deleteAll();
        movieRepository.deleteAll();
    }

    @Test
    void findConflictingProgrammes_sameHallSameDate_returnsConflicts() {
        //given
        Programme programme1 = underTest.save(new Programme(movie, date, time, cinemaHallA));
        Programme programme2 = underTest.save(new Programme(movie, date, time, cinemaHallA));
        Programme programme3 = underTest.save(new Programme(movie, date, time, cinemaHallA));

        List<Programme> conflictingProgrammes = Arrays.asList(programme2, programme3);

        //when
        List<Programme> programmes = underTest.findConflictingProgrammes("A", date, programme1.getId());
        programmes.forEach(p ->
                System.out.println(p.getId()));

        //then
        assertThat(programmes).hasSameElementsAs(conflictingProgrammes);
    }

    @Test
    void findConflictingProgrammes_differentHall_returnsEmptyList() {
        //given
        Programme programme1 =  underTest.save(new Programme(movie, date, time, cinemaHallA));
        underTest.save(new Programme(movie, date, time, cinemaHallB));
        underTest.save(new Programme(movie, date, time, cinemaHallB));

        //when
        List<Programme> programmes = underTest.findConflictingProgrammes("A", date, programme1.getId());

        //then
        assertThat(programmes).isEmpty();
    }

    @Test
    void findConflictingProgrammes_differentDate_returnsEmptyList() {
        //given
        Programme programme1 =  underTest.save(new Programme(movie, date, time, cinemaHallA));
        underTest.save(new Programme(movie, otherDate, time, cinemaHallA));
        underTest.save(new Programme(movie, otherDate, time, cinemaHallA));

        //when
        List<Programme> programmes = underTest.findConflictingProgrammes("A", date, programme1.getId());

        //then
        assertThat(programmes).isEmpty();
    }

    @Test
    void findConflictingProgrammes_differentDate_and_differentHall_returnsEmptyList() {
        //given
        Programme programme1 =  underTest.save(new Programme(movie, date, time, cinemaHallA));
        underTest.save(new Programme(movie, otherDate, time, cinemaHallB));
        underTest.save(new Programme(movie, otherDate, time, cinemaHallB));

        //when
        List<Programme> programmes = underTest.findConflictingProgrammes("A", date, programme1.getId());

        //then
        assertThat(programmes).isEmpty();
    }

    @Test
    void findByDate_existingProgrammes_returnsProgrammeList() {
        //given
        Programme programme1 =  underTest.save(new Programme(movie, date, time, cinemaHallA));
        Programme programme2 =  underTest.save(new Programme(movie, date, otherTime, cinemaHallA));

        List<Programme> existingProgrammes = Arrays.asList(programme1, programme2);

        //when
        List<Programme> programmes = underTest.findByDate(date);

        //then
        assertThat(programmes).hasSameElementsAs(existingProgrammes);
    }

    @Test
    void findByDate_noProgrammes_returnsEmptyList() {
        //when
        List<Programme> programmes = underTest.findByDate(date);

        //then
        assertThat(programmes).isEmpty();
    }


    @Test
    void findByTitleDateHallName_returnsProgrammeList() {
        //given
        Programme programme1 =  underTest.save(new Programme(movie, date, time, cinemaHallA));
        Programme programme2 =  underTest.save(new Programme(movie, date, otherTime, cinemaHallA));

        List<Programme> existingProgrammes = Arrays.asList(programme1, programme2);

        //when
        List<Programme> programmes = underTest.findByTitleDateHallName(movie.getTitle(), date, cinemaHallA.getName());

        //then
        assertThat(programmes).hasSameElementsAs(existingProgrammes);
    }

    @Test
    void findByTitleDateHallName_hallNameNull_returnsProgrammeList() {
        //given
        Programme programme1 =  underTest.save(new Programme(movie, date, time, cinemaHallA));
        Programme programme2 =  underTest.save(new Programme(movie, date, otherTime, cinemaHallA));

        List<Programme> existingProgrammes = Arrays.asList(programme1, programme2);

        //when
        List<Programme> programmes = underTest.findByTitleDateHallName(movie.getTitle(), date, null);

        //then
        assertThat(programmes).hasSameElementsAs(existingProgrammes);
    }

    @Test
    void findByTitleDateHallName_returnsProgrammeList_returnsEmptyList() {
        //when
        List<Programme> programmes = underTest.findByTitleDateHallName(movie.getTitle(), date, cinemaHallA.getName());

        //then
        assertThat(programmes).isEmpty();
    }
}