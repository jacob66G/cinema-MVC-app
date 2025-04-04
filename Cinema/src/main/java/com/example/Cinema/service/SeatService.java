package com.example.Cinema.service;

import com.example.Cinema.mapper.SeatMapper;
import com.example.Cinema.model.Programme;
import com.example.Cinema.model.Seat;
import com.example.Cinema.model.dto.SeatDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SeatService {

    private final TicketService ticketService;
    private final SeatMapper seatMapper;

    public SeatService(TicketService ticketService, SeatMapper seatMapper) {
        this.ticketService = ticketService;
        this.seatMapper = seatMapper;
    }

    public List<SeatDto> getSeatsWithBookingStatus(Programme programme) {
        List<Seat> bookedSeats = ticketService.getBookedSeats(programme);

        return programme.getCinemaHall().getSeats().stream()
                .map(seat -> seatMapper.toDto(seat, bookedSeats.contains(seat)))
                .toList();
    }
}
