package com.example.Cinema.service.Validators;

import com.example.Cinema.exception.ValidationException;
import com.example.Cinema.model.Ticket;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationValidationService {

    public void isEmailValid(String email, String confirmedEmail) {
        if(!email.equals(confirmedEmail)) {
            throw new ValidationException("Adresy email różnią się");
        }
    }

    public void areTicketsValid(List<Ticket> tickets) {
        if(tickets.stream().anyMatch(ticket -> ticket.getTicketType() == null)) {
            throw new ValidationException("Nie wybrano typu bietów");
        }
    }
}
