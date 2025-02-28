package com.example.Cinema.repository;

import com.example.Cinema.model.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class TicketRepositoryTest {

    @Autowired
    TicketRepository underTest;

    @Autowired
    ProgrammeRepository programmeRepository;

    @Autowired
    MovieRepository movieRepository;

    @Autowired
    CinemaHallRepository cinemaHallRepository;

    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    SeatRepository seatRepository;

    @Test
    void getBookedSeats() {
        //given
        LocalDate date = LocalDate.of(2020, 1, 1);
        LocalTime time = LocalTime.of(12, 0);
        Movie movie = new Movie("test", "test", 100);
        movieRepository.save(movie);

        CinemaHall cinemaHall = new CinemaHall("A");
        cinemaHallRepository.save(cinemaHall);

        Programme programme = new Programme(movie, date, time, cinemaHall);
        programmeRepository.save(programme);

        Reservation reservation = new Reservation(LocalDateTime.now(), "test", "test", "test", "test", 10.0);
        reservationRepository.save(reservation);

        Seat seat1 = new Seat("H", 1, cinemaHall);
        Seat seat2 = new Seat("H", 2, cinemaHall);
        Seat seat3 = new Seat("H", 3, cinemaHall);

        seatRepository.save(seat1);
        seatRepository.save(seat2);
        seatRepository.save(seat3);

        List<Seat> seats = List.of(seat1, seat2, seat3);

        Ticket ticket1 = new Ticket(programme, reservation, seat1,"normalny", 20.0);
        Ticket ticket2 = new Ticket(programme, reservation, seat2,"normalny", 20.0);
        Ticket ticket3 = new Ticket(programme, reservation, seat3,"normalny", 20.0);

        underTest.save(ticket1);
        underTest.save(ticket2);
        underTest.save(ticket3);

        //when
        List<Seat> bookedSeats = underTest.getBookedSeats(programme);

        //then
        assertThat(bookedSeats).hasSameElementsAs(seats);
    }
}