package com.example.Cinema.repository;

import com.example.Cinema.model.Movie;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class MovieRepositoryTest {

    @Autowired
    MovieRepository underTest;

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }

    @Test
    void existsByTitle_existsMovieWithTitle_returnsTrue() {
        //given
        Movie movie = new Movie("Avatar", "test description", 120);
        underTest.save(movie);

        //when
        boolean expected = underTest.existsByTitle("Avatar", 3L);

        //then
        assertTrue(expected);
    }

    @Test
    void existsByTitle_noMovieWithTitle_returnsFalse() {
        //given
        String title = "Avatar";

        //when
        boolean expected = underTest.existsByTitle(title, 1L);

        //then
        assertFalse(expected);
    }
}