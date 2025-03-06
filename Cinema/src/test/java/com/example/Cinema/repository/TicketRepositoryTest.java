package com.example.Cinema.repository;

import com.example.Cinema.model.*;
import org.aspectj.lang.annotation.After;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
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

    Movie movie;
    CinemaHall cinemaHall;
    Seat seat1;
    Seat seat2;
    Seat seat3;

    @BeforeEach
    void setUp() {
        movie = movieRepository.save(new Movie("test", "test", 100));
        cinemaHall = cinemaHallRepository.save(new CinemaHall("A"));
        seat1 = seatRepository.save(new Seat("H", 1, cinemaHall));
        seat2 = seatRepository.save(new Seat("H", 2, cinemaHall));
        seat3 = seatRepository.save(new Seat("H", 3, cinemaHall));
    }

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
        programmeRepository.deleteAll();
        movieRepository.deleteAll();
        cinemaHallRepository.deleteAll();
        reservationRepository.deleteAll();
        seatRepository.deleteAll();
    }

    @Test
    void  getBookedSeats_existingBookings_returnsSeatList() {
        //given
        LocalDate date = LocalDate.of(2020, 1, 1);
        LocalTime time = LocalTime.of(12, 0);

        Programme programme = programmeRepository.save(new Programme(movie, date, time, cinemaHall));
        Reservation reservation = reservationRepository.save(new Reservation(LocalDateTime.now(), "test", "test", "test", "test", 10.0));

        underTest.save(new Ticket(programme, reservation, seat1,"normalny", 20.0));
        underTest.save(new Ticket(programme, reservation, seat2,"normalny", 20.0));
        underTest.save(new Ticket(programme, reservation, seat3,"normalny", 20.0));

        List<Seat> seats = Arrays.asList(seat1, seat2, seat3);

        //when
        List<Seat> bookedSeats = underTest.getBookedSeats(programme);

        //then
        assertThat(bookedSeats).hasSameElementsAs(seats);
    }

    @Test
    void getBookedSeats_noBookings_returnsEmptyList() {
        //given
        LocalDate date = LocalDate.of(2020, 1, 1);
        LocalTime time = LocalTime.of(12, 0);

        Programme programme = programmeRepository.save(new Programme(movie, date, time, cinemaHall));

        //when
        List<Seat> bookedSeats = underTest.getBookedSeats(programme);

        //then
        assertThat(bookedSeats).isEmpty();
    }

    @Test
    void findAllByProgramme_Idprogramme_existsTickets_returnsTickets() {
        //given
        LocalDate date = LocalDate.of(2020, 1, 1);
        LocalTime time = LocalTime.of(12, 0);

        Programme programme = programmeRepository.save(new Programme(movie, date, time, cinemaHall));

        Reservation reservation = reservationRepository.save(new Reservation(LocalDateTime.now(), "test", "test", "test", "test", 10.0));

        Ticket ticket1 = underTest.save(new Ticket(programme, reservation, seat1,"normalny", 20.0));
        Ticket ticket2 = underTest.save(new Ticket(programme, reservation, seat2,"normalny", 20.0));
        Ticket ticket3 = underTest.save(new Ticket(programme, reservation, seat3,"normalny", 20.0));

        List<Ticket> existsTickets = Arrays.asList(ticket1, ticket2, ticket3);

        //when
        List<Ticket> tickets = underTest.findAllByProgramme_Idprogramme(programme.getIdprogramme());

        //then
        assertThat(tickets).hasSameElementsAs(existsTickets);
    }

    @Test
    void findAllByProgramme_Idprogramme_noTickets_returnsEmptyList() {
        //given
        LocalDate date = LocalDate.of(2020, 1, 1);
        LocalTime time = LocalTime.of(12, 0);

        Programme programme = programmeRepository.save(new Programme(movie, date, time, cinemaHall));

        //when
        List<Ticket> tickets = underTest.findAllByProgramme_Idprogramme(programme.getIdprogramme());

        //then
        assertThat(tickets).isEmpty();
    }

    @Test
    void findAllByProgramme_Movie_Idmovie_existsTickets_returnsTickets() {
        //given
        LocalDate date = LocalDate.of(2020, 1, 1);
        LocalTime time = LocalTime.of(12, 0);
        Programme programme = programmeRepository.save(new Programme(movie, date, time, cinemaHall));

        Reservation reservation = reservationRepository.save(new Reservation(LocalDateTime.now(), "test", "test", "test", "test", 10.0));

        Ticket ticket1 = underTest.save(new Ticket(programme, reservation, seat1,"normalny", 20.0));
        Ticket ticket2 = underTest.save(new Ticket(programme, reservation, seat2,"normalny", 20.0));
        Ticket ticket3 = underTest.save(new Ticket(programme, reservation, seat3,"normalny", 20.0));

        List<Ticket> existsTickets = Arrays.asList(ticket1, ticket2, ticket3);

        //when
        List<Ticket> tickets = underTest.findAllByProgramme_Movie_Idmovie(movie.getIdmovie());

        //then
        assertThat(tickets).hasSameElementsAs(existsTickets);
    }

    @Test
    void findAllByProgramme_Movie_Idmovie_noTickets_returnsEmptyList() {
        //when
        List<Ticket> tickets = underTest.findAllByProgramme_Movie_Idmovie(movie.getIdmovie());

        //then
        assertThat(tickets).isEmpty();
    }
}