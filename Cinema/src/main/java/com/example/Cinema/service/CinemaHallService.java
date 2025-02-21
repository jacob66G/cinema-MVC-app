package com.example.Cinema.service;

import com.example.Cinema.model.CinemaHall;
import com.example.Cinema.repository.CinemaHallRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CinemaHallService {

    private final CinemaHallRepository cinemaHallRepository;

    @Autowired
    public CinemaHallService(CinemaHallRepository cinemaHallRepository) {
        this.cinemaHallRepository = cinemaHallRepository;
    }

    public List<CinemaHall> getAllCinemaHalls() {
        return cinemaHallRepository.findAll();
    }

    public void save(CinemaHall cinemaHall) {
        cinemaHallRepository.save(cinemaHall);
    }

}
