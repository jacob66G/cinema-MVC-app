package com.example.Cinema.service.Validators;

import com.example.Cinema.model.Ticket;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationValidationService {

    public boolean isEmailValid(String email, String confirmedEmail) {
        return email.equals(confirmedEmail);
    }

    public boolean areTicketsValid(List<Ticket> tickets) {
        return tickets.stream().allMatch(ticket -> ticket.getTicketType() != null);
    }
}
