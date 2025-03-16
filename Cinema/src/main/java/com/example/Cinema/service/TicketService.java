package com.example.Cinema.service;

import com.example.Cinema.model.Programme;
import com.example.Cinema.model.Seat;
import com.example.Cinema.model.Ticket;
import com.example.Cinema.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;

    @Autowired
    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }
    public List<Seat> getBookedSeats(Programme programme) {
        return ticketRepository.getBookedSeats(programme);
    }
}
