package com.example.Cinema.service;

import com.example.Cinema.mapper.SeatMapper;
import com.example.Cinema.model.CinemaHall;
import com.example.Cinema.model.Programme;
import com.example.Cinema.model.Seat;
import com.example.Cinema.model.dto.SeatDto;
import com.example.Cinema.repository.CinemaHallRepository;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CinemaHallService {

    private final CinemaHallRepository cinemaHallRepository;

    @Autowired
    public CinemaHallService(CinemaHallRepository cinemaHallRepository, TicketService ticketService, SeatMapper seatMapper) {
        this.cinemaHallRepository = cinemaHallRepository;
    }

    public List<CinemaHall> getAllCinemaHalls() {
        return cinemaHallRepository.findAll();
    }

    public void save(CinemaHall cinemaHall) {
        cinemaHallRepository.save(cinemaHall);
    }
}



