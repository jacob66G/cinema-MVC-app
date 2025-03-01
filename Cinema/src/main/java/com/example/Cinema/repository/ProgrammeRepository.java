package com.example.Cinema.repository;

import com.example.Cinema.model.Programme;
import jakarta.validation.constraints.NotNull;
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

    @Query("SELECT p FROM Programme p WHERE p.cinemaHall.name = :hallName AND p.date = :date AND p.idprogramme != :id")
    List<Programme> findConflictingProgrammes(
            @Param("hallName") String cinemaHallName,
            @Param("date") LocalDate date,
            @Param("id") Long id);

    List<Programme> findByDate(LocalDate date);

    List<Programme> findByCinemaHall_Name(String hallName);

    List<Programme> findByDateAndCinemaHall_Name(LocalDate date, String hallName);
}
