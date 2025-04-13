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

    @Query("SELECT t.seat FROM Ticket t WHERE t.reservation.programme.id = :id")
    List<Seat> getBookedSeats(@Param("id") Long id);

    @Query("SELECT t FROM Ticket t WHERE t.reservation.programme.id = :id")
    List<Ticket> findAllByProgramme_Id(@Param("id") Long id);

    @Query("SELECT t FROM Ticket t WHERE t.reservation.programme.movie.id = :id")
    List<Ticket> findAllByProgramme_Movie_Id(@Param("id") Long id);
}
