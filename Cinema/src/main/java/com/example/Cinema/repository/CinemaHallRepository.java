package com.example.Cinema.repository;

import com.example.Cinema.model.CinemaHall;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CinemaHallRepository extends JpaRepository <CinemaHall, String> {
    CinemaHall findCinemaHallByName(String name);
}
