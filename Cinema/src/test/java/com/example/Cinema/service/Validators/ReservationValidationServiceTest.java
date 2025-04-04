package com.example.Cinema.service.Validators;

import com.example.Cinema.exception.ValidationException;
import com.example.Cinema.model.Ticket;
import com.example.Cinema.model.enums.TicketCategory;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReservationValidationServiceTest {

    ReservationValidationService underTest = new ReservationValidationService();

    @Test
    void shouldThrowException_WhenEmailsAreNotEqual() {
        //given
        String email = "test@test.com";
        String confirmedEmail = "test123@test.com";

        //when + then
        ValidationException ex = assertThrows(ValidationException.class, () -> underTest.isEmailValid(email, confirmedEmail));

        assertEquals("Adresy email różnią się", ex.getMessage());
    }

    @Test
    void shouldDoNothing_WhenEmailsAreEqual() {
        //given
        String email = "test@test.com";
        String confirmedEmail = "test@test.com";

        //when + throw
        assertDoesNotThrow(() -> underTest.isEmailValid(email, confirmedEmail));
    }

    @Test
    void shouldThrowException_WhenTicketsTypeAreNull() {
        //given
        Ticket ticket = new Ticket();
        Ticket ticket2 = new Ticket();
        ticket2.setTicketType(TicketCategory.NORMALNY);

        //when + then
        ValidationException ex = assertThrows(ValidationException.class, () -> underTest.areTicketsValid(List.of(ticket, ticket2)));

        assertEquals("Nie wybrano typu bietów", ex.getMessage());
    }

    @Test
    void shouldDoNothing_WhenTicketsTypeAreNotNull() {
        //given
        Ticket ticket = new Ticket();
        ticket.setTicketType(TicketCategory.NORMALNY);

        //when + then
        assertDoesNotThrow(() -> underTest.areTicketsValid(List.of(ticket)));
    }
}