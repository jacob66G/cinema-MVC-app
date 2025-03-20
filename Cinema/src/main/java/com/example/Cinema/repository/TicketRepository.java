package com.example.Cinema.repository;

import com.example.Cinema.model.Programme;
import com.example.Cinema.model.Seat;
import com.example.Cinema.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    @Query("SELECT t.seat FROM Ticket t WHERE t.programme = :programme")
    List<Seat> getBookedSeats(@Param("programme") Programme programme);

    List<Ticket> findAllByProgramme_Id(Long id);

    List<Ticket> findAllByProgramme_Movie_Id(Long id);
}
