package com.example.Cinema.service;

import com.example.Cinema.exception.ReservationNotFoundException;
import com.example.Cinema.mapper.ReservationMapper;
import com.example.Cinema.model.User;
import com.example.Cinema.model.dto.ReservationDto;
import com.example.Cinema.model.dto.SeatDto;
import com.example.Cinema.model.dto.SeatListDto;
import com.example.Cinema.model.Programme;
import com.example.Cinema.model.Reservation;
import com.example.Cinema.model.Ticket;
import com.example.Cinema.repository.ReservationRepository;
import com.example.Cinema.repository.SeatRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final SeatRepository seatRepository;
    private final ReservationMapper reservationMapper;

    @Autowired
    public ReservationService(ReservationRepository reservationRepository, SeatRepository seatRepository, ReservationMapper reservationMapper) {
        this.reservationRepository = reservationRepository;
        this.seatRepository = seatRepository;
        this.reservationMapper = reservationMapper;
    }

    public void save(ReservationDto reservationDto) {
        reservationRepository.save(reservationMapper.fromDto(reservationDto));
    }


    public ReservationDto createReservationDto(Programme programme, SeatListDto seatListDto) {
        List<SeatDto> selectedSeats = seatListDto.getSeats().stream()
                .filter(SeatDto::isChosen)
                .toList();

        List<Ticket> tickets = selectedSeats.stream()
                .map(seatDto -> seatRepository.findById(seatDto.getId())
                        .map(seat -> {
                            Ticket ticket = new Ticket();
                            ticket.setProgramme(programme);
                            ticket.setSeat(seat);
                            return ticket;
                        })
                        .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono miejsca o ID: " + seatDto.getId())))
                .toList();

        ReservationDto reservationDto = new ReservationDto();
        reservationDto.setTickets(tickets);
        reservationDto.setProgramme(programme);

        return reservationDto;
    }

    public List<ReservationDto> getReservationsDto(User user) {
        return reservationRepository.findAllByUser(user).stream().map(reservationMapper::toDto).toList();
    }

    public void delete(Long id) {
        reservationRepository.deleteById(id);
    }

    public Reservation getReservationDetails(Long id) {

        return reservationRepository.findById(id).orElseThrow(() -> new ReservationNotFoundException(id));
    }
}
