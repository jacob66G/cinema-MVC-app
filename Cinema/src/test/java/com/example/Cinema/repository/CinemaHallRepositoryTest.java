package com.example.Cinema.repository;

import com.example.Cinema.model.CinemaHall;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CinemaHallRepositoryTest {

    @Autowired
    private CinemaHallRepository underTest;

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }

    @Test
    void shouldReturnCinemaHallByName() {
        //given
        CinemaHall cinemaHall = new CinemaHall("A");
        underTest.save(cinemaHall);

        //when
        CinemaHall expected = underTest.findCinemaHallByName("A");

        //then
        assertThat(expected.getName()).isEqualTo(cinemaHall.getName());
    }

    @Test
    void shouldNotReturnCinemaHallByName() {
        //given
        CinemaHall cinemaHall = new CinemaHall("B");
        underTest.save(cinemaHall);

        //when
        CinemaHall expected = underTest.findCinemaHallByName("A");

        //then
        assertThat(expected).isNull();
    }
}