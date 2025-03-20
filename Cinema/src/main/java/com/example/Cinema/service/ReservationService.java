package com.example.Cinema.service;

import com.example.Cinema.model.dto.ReservationDto;
import com.example.Cinema.model.dto.SeatDto;
import com.example.Cinema.model.dto.SeatListDto;
import com.example.Cinema.model.Programme;
import com.example.Cinema.model.Reservation;
import com.example.Cinema.model.Ticket;
import com.example.Cinema.repository.ReservationRepository;
import com.example.Cinema.repository.SeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final SeatRepository seatRepository;

    @Autowired
    public ReservationService(ReservationRepository reservationRepository, SeatRepository seatRepository) {
        this.reservationRepository = reservationRepository;
        this.seatRepository = seatRepository;
    }

    public void save(Reservation reservation) {
        reservationRepository.save(reservation);
    }


    public ReservationDto createReservationDto(Programme programme, SeatListDto seatListDto) {
        List<SeatDto> selectedSeats = seatListDto.getSeats().stream()
                .filter(SeatDto::isChosen)
                .toList();

        if (selectedSeats.isEmpty()) {
            throw new IllegalArgumentException("Nie wybrano miejsca!");
        }

        List<Ticket> tickets = selectedSeats.stream()
                .map(seatDto -> seatRepository.findById(seatDto.getId())
                        .map(seat -> {
                            Ticket ticket = new Ticket();
                            ticket.setProgramme(programme);
                            ticket.setSeat(seat);
                            return ticket;
                        })
                        .orElseThrow(() -> new NoSuchElementException("Nie znaleziono miejsca o ID: " + seatDto.getId())))
                .toList();

        ReservationDto reservationDto = new ReservationDto();
        reservationDto.setTickets(tickets);
        reservationDto.setProgramme(programme);

        return reservationDto;
    }
}
