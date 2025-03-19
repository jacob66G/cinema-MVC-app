package com.example.Cinema.mapper;

import com.example.Cinema.model.dto.ReservationDto;
import com.example.Cinema.model.Reservation;
import com.example.Cinema.model.Ticket;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class ReservationMapper {

    public Reservation fromDto(ReservationDto dto) {
        Reservation reservation = new Reservation();
        reservation.setReservationDate(LocalDateTime.now());
        reservation.setClientName(dto.getClientName());
        reservation.setClientSurname(dto.getClientSurname());
        reservation.setClientAddressEmail(dto.getClientAddressEmail());
        reservation.setClientPhoneNumber(dto.getClientPhoneNumber());
        reservation.setPrice(dto.getTotalPrice());

        List<Ticket> tickets = dto.getTickets();
        tickets.forEach(ticket -> ticket.setReservation(reservation));
        reservation.setTickets(tickets);

        return reservation;
    }
}
