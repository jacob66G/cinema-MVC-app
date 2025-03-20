package com.example.Cinema.service.Validators;

import com.example.Cinema.model.Ticket;
import com.example.Cinema.model.enums.TicketCategory;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReservationValidationServiceTest {

    ReservationValidationService reservationValidationService = new ReservationValidationService();

    @Test
    void isEmailValid_sameEmail_returnTrue() {
        //given
        String email = "test@test.com";
        String confirmedEmail = "test@test.com";

        //when
        boolean result = reservationValidationService.isEmailValid(email, confirmedEmail);

        //then
        assertTrue(result);
    }

    @Test
    void isEmailValid_differentEmail_returnFalse() {
        //given
        String email = "test@test.com";
        String confirmedEmail = "differentEmail@.com";

        //when
        boolean result = reservationValidationService.isEmailValid(email, confirmedEmail);

        //then
        assertFalse(result);
    }

    @Test
    void areTicketsValid_allTicketsHaveType_returnTrue() {
        //given
        Ticket ticket = new Ticket();
        ticket.setTicketType(TicketCategory.NORMALNY);

        //when
        boolean result = reservationValidationService.areTicketsValid(List.of(ticket));

        //then
        assertTrue(result);
    }

    @Test
    void areTicketsValid_anyTicketsHaveType_returnFalse() {
        //given
        Ticket ticket = new Ticket();

        //when
        boolean result = reservationValidationService.areTicketsValid(List.of(ticket));

        //then
        assertFalse(result);
    }

    @Test
    void areTicketsValid_notAllTicketsHaveType_returnFalse() {
        //given
        Ticket ticket = new Ticket();
        Ticket ticket2 = new Ticket();
        ticket2.setTicketType(TicketCategory.NORMALNY);

        //when
        boolean result = reservationValidationService.areTicketsValid(List.of(ticket, ticket2));

        //then
        assertFalse(result);
    }
}