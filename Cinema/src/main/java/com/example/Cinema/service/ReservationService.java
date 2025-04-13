package com.example.Cinema.service;

import com.example.Cinema.exception.ProgrammeNotFoundException;
import com.example.Cinema.exception.ReservationNotFoundException;
import com.example.Cinema.exception.SeatNotFoundException;
import com.example.Cinema.mapper.ReservationMapper;
import com.example.Cinema.model.*;
import com.example.Cinema.model.dto.ReservationDto;
import com.example.Cinema.model.dto.SeatDto;
import com.example.Cinema.model.dto.SeatListDto;
import com.example.Cinema.model.dto.TicketDto;
import com.example.Cinema.repository.PriceRepository;
import com.example.Cinema.repository.ProgrammeRepository;
import com.example.Cinema.repository.ReservationRepository;
import com.example.Cinema.repository.SeatRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final PriceRepository priceRepository;
    private final ReservationMapper reservationMapper;
    private final ProgrammeRepository programmeRepository;
    private final SeatRepository seatRepository;

    @Autowired
    public ReservationService(
            ReservationRepository reservationRepository,
            PriceRepository priceRepository,
            ReservationMapper reservationMapper,
            ProgrammeRepository programmeRepository,
            SeatRepository seatRepository
    ) {
        this.reservationRepository = reservationRepository;
        this.priceRepository = priceRepository;
        this.reservationMapper = reservationMapper;
        this.programmeRepository = programmeRepository;
        this.seatRepository = seatRepository;
    }

    @Transactional
    public void createReservation(ReservationDto dto) {
        Reservation reservation = reservationMapper.fromDto(dto);
        reservation.setProgramme(getProgramme(dto.getProgramme().getId()));

        List<Ticket> tickets = mapTickets(dto.getTickets());
        tickets.forEach(reservation::addTicket);

        reservation.setReservationDate(LocalDateTime.now());

        reservationRepository.save(reservation);
    }

    public void delete(Long id) {
        reservationRepository.deleteById(id);
    }

    public Reservation getReservationDetails(Long id) {
        return reservationRepository.findById(id).orElseThrow(() -> new ReservationNotFoundException(id));
    }

    public List<Reservation> getReservations(User user) {
        return reservationRepository.findAllByUser(user);
    }

    public Reservation getReservationById(Long id) {
        return reservationRepository.findById(id).orElseThrow(() -> new ReservationNotFoundException(id));
    }
    public void assignTicketsToReservation(SeatListDto seatListDto, ReservationDto reservationDto) {
        List<SeatDto> selectedSeats = seatListDto.getSeats().stream()
                .filter(SeatDto::isChosen)
                .toList();

        List<TicketDto> ticketDtos = selectedSeats.stream()
                .map(seatDto -> {
                    TicketDto ticketDto = new TicketDto();
                    ticketDto.setSeat(seatDto);
                    return ticketDto;
                }).toList();

        reservationDto.setTickets(ticketDtos);
    }

    public void applyPrices(List<TicketDto> dto) {
        for(TicketDto ticketDto : dto) {
            Price price = priceRepository.getPriceByType(ticketDto.getTicketType());
            if(price != null) {
                ticketDto.setPrice(price.getPriceValue());
            }
        }
    }

    public double getTotalPrice(ReservationDto dto) {
        return dto.getTickets().stream().mapToDouble(TicketDto::getPrice).sum();
    }

    private Programme getProgramme(Long programmeId) {
        return programmeRepository.findById(programmeId)
                .orElseThrow(() -> new ProgrammeNotFoundException(programmeId));
    }

    private List<Ticket> mapTickets(List<TicketDto> ticketDtos) {
        return ticketDtos.stream()
                .map(ticketDto -> {
                    Ticket ticket = new Ticket();
                    ticket.setId(ticketDto.getId());
                    ticket.setTicketType(ticketDto.getTicketType());
                    ticket.setPrice(ticketDto.getPrice());
                    Seat seat = seatRepository.findById(ticketDto.getSeat().getId())
                            .orElseThrow(() -> new SeatNotFoundException(ticketDto.getSeat().getId()));
                    ticket.setSeat(seat);
                    return ticket;
                }).toList();
    }
}
