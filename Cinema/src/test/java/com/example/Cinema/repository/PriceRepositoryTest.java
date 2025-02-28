package com.example.Cinema.repository;

import com.example.Cinema.model.Price;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class PriceRepositoryTest {

    @Autowired
    private PriceRepository underTest;

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }

    @Test
    void itShouldReturnPriceByType() {
        //given
        Price price = new Price("normalny", 20.0);
        underTest.save(price);

        //when
        Price expected = underTest.getPriceByType("normalny");

        //then
        assertThat(expected).isEqualTo(price);
    }

    @Test
    void itShouldNotReturnPriceByType() {
        //given
        String type = "normalny";

        //when
        Price expected = underTest.getPriceByType(type);

        //then
        assertThat(expected).isNull();
    }
}