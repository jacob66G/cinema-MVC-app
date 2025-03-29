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

        if(dto.getUser() != null) {
            reservation.setUser(dto.getUser());
        }

        List<Ticket> tickets = dto.getTickets();
        tickets.forEach(ticket -> ticket.setReservation(reservation));
        reservation.setTickets(tickets);

        return reservation;
    }

    public ReservationDto toDto(Reservation reservation) {
        ReservationDto dto = new ReservationDto();
        dto.setReservationDate(reservation.getReservationDate());
        dto.setClientName(reservation.getClientName());
        dto.setClientSurname(reservation.getClientSurname());
        dto.setClientAddressEmail(reservation.getClientAddressEmail());
        dto.setClientPhoneNumber(reservation.getClientPhoneNumber());
        dto.setId(reservation.getId());

        if(reservation.getUser() != null) {
            dto.setUser(reservation.getUser());
        }

        dto.setTickets(reservation.getTickets());

        dto.setProgramme(reservation.getTickets().get(0).getProgramme());

        double price = reservation.getTickets().stream().mapToDouble(Ticket::getPrice).sum();
        dto.setPrice(price);

        return dto;
    }
}
