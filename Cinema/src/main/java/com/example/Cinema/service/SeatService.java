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
    private final ProgrammeService programmeService;

    public SeatService(TicketService ticketService, SeatMapper seatMapper, ProgrammeService programmeService) {
        this.ticketService = ticketService;
        this.seatMapper = seatMapper;
        this.programmeService = programmeService;
    }

    public List<SeatDto> getSeatsWithBookingStatus(Long programmeId) {
        Programme programme = programmeService.getProgrammeById(programmeId);
        List<Seat> bookedSeats = ticketService.getBookedSeats(programmeId);

        return programme.getCinemaHall().getSeats().stream()
                .map(seat -> seatMapper.toDto(seat, bookedSeats.contains(seat)))
                .toList();
    }
}
