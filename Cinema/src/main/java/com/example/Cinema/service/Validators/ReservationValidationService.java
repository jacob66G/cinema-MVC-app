package com.example.Cinema.service.Validators;

import com.example.Cinema.exception.ValidationException;
import com.example.Cinema.model.dto.ReservationDto;
import com.example.Cinema.model.dto.TicketDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationValidationService {

    public void validate(ReservationDto reservationDto) {
        isEmailValid(reservationDto.getClientAddressEmail(), reservationDto.getConfirmedClientAddressEmail());
        areTicketsValid(reservationDto.getTickets());
    }

    private void isEmailValid(String email, String confirmedEmail) {
        if(!email.equals(confirmedEmail)) {
            throw new ValidationException("Adresy email różnią się");
        }
    }

    private void areTicketsValid(List<TicketDto> tickets) {
        if(tickets.stream().anyMatch(ticket -> ticket.getTicketType() == null)) {
            throw new ValidationException("Nie wybrano typu bietów");
        }
    }
}
