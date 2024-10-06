package com.example.Cinema.repository;

import com.example.Cinema.model.Programme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProgrammeRepository extends JpaRepository<Programme, Long> {

    @Query("SELECT p FROM Programme p WHERE p.date = :date")
    List<Programme> findByDate(@Param("date") LocalDate date);

    @Query("SELECT p FROM Programme p WHERE p.movie.idmovie = :id AND p.date = :date AND p.time = :time AND p.cinemaHall.name = :hall")
    Optional<Programme> findByIdMovieAndDataAndTimeAndCinemaHall(Long id, LocalDate date, LocalTime time, String hall);

    @Query("SELECT p FROM Programme p WHERE p.date = :date AND p.cinemaHall.name = :hall")
    List<Programme> findByDateAndHall(LocalDate date, String hall);
}
