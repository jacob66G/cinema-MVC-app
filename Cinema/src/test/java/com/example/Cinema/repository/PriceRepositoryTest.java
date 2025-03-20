package com.example.Cinema.repository;

import com.example.Cinema.model.Price;
import com.example.Cinema.model.enums.TicketCategory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class PriceRepositoryTest {

    @Autowired
    private PriceRepository underTest;

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }

    @Test
    void getPriceByType_existsPriceWithType_returnsPrice() {
        //given
        Price price = new Price(TicketCategory.NORMALNY, 20.0);
        underTest.save(price);

        //when
        Price expected = underTest.getPriceByType(TicketCategory.NORMALNY);

        //then
        assertThat(expected).isEqualTo(price);
    }

    @Test
    void getPriceByType_noPriceWithType_returnsNull() {
        //given
        TicketCategory priceType = TicketCategory.NORMALNY;

        //when
        Price expected = underTest.getPriceByType(priceType);

        //then
        assertThat(expected).isNull();
    }
}