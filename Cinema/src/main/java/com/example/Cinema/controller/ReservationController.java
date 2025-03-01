package com.example.Cinema.controller;

import com.example.Cinema.model.*;
import com.example.Cinema.model.Dto.ProgrammeDto;
import com.example.Cinema.model.Dto.ReservationDto;
import com.example.Cinema.model.Dto.SeatDto;
import com.example.Cinema.model.Dto.SeatListDto;
import com.example.Cinema.repository.SeatRepository;
import com.example.Cinema.service.PriceService;
import com.example.Cinema.service.ProgrammeService;
import com.example.Cinema.service.ReservationService;
import com.example.Cinema.service.TicketService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.*;


@Controller
@RequestMapping("/reservation")
@SessionAttributes({"selectedProgramme","programmeDto", "reservationDto"})
public class ReservationController {

    private final ReservationService reservationService;
    private final ProgrammeService programmeService;
    private final TicketService ticketService;
    private final PriceService priceService;
    private final SeatRepository seatRepository;

    @Autowired
    public ReservationController(
            ReservationService reservationService,
            ProgrammeService programmeService,
            TicketService ticketService,
            PriceService priceService,
            SeatRepository seatRepository
    ) {
        this.reservationService = reservationService;
        this.programmeService = programmeService;
        this.ticketService = ticketService;
        this.priceService = priceService;
        this.seatRepository = seatRepository;
    }

    @GetMapping()
    public String getReservationPage(@RequestParam(required = false) Long id, Model model) {
        Optional<Programme> programme = programmeService.getProgrammeById(id);

        List<Seat> bookedSeats = ticketService.getBookedSeats(programme.get());
        List<Seat> seats = programme.get().getCinemaHall().getSeats();

        List<SeatDto> seatsDto = seats.stream().map(seat -> {
            boolean bookedSeat = bookedSeats.contains(seat);

            return new SeatDto(
                    seat.getIdseat(),
                    seat.getRow(),
                    seat.getNumber(),
                    bookedSeat,
                    false
            );
        }).toList();

        Movie movie = programme.get().getMovie();
        ProgrammeDto programmeDto = new ProgrammeDto();
        programmeDto.setId(id);
        programmeDto.setDate(programme.get().getDate());
        programmeDto.setTime(programme.get().getTime());
        programmeDto.setMovieTitle(movie.getTitle());

        String base64Image = Base64.getEncoder().encodeToString(movie.getImageData());
        programmeDto.setMovieBase64Image(base64Image);


        model.addAttribute("selectedProgramme", programme);
        model.addAttribute("seats", seatsDto);
        model.addAttribute("programmeDto", programmeDto);
        model.addAttribute("seatsForm", new SeatListDto(seatsDto));

        return "reservation";
    }

    @PostMapping()
    public String seatsForm(
            @ModelAttribute("selectedProgramme") Programme programme,
            @ModelAttribute("seatsForm") SeatListDto seatListDto,
            RedirectAttributes redirectAttributes,
            Model model
    ) {

        List<SeatDto> selectedSeats = seatListDto.getSeats().stream().filter(SeatDto::isChosen
        ).toList();

        if(selectedSeats.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Nie wybrano miejsca!");
            return "redirect:/reservation?id=" + programme.getIdprogramme();
        }

        ReservationDto reservationDto = new ReservationDto();
        List<Ticket> tickets = new ArrayList<>();

        for(SeatDto seatDto : selectedSeats) {
            Optional<Seat> seat = seatRepository.findById(seatDto.getIdseat());

            if(seat.isPresent()) {
                Ticket ticket = new Ticket();
                ticket.setProgramme(programme);
                ticket.setSeat(seat.get());

                tickets.add(ticket);
            }
        }

        System.out.println(tickets);
        reservationDto.setTickets(tickets);
        reservationDto.setProgramme(programme);

        model.addAttribute("selectedSeats", selectedSeats);
        model.addAttribute("reservationDto", reservationDto);

        return "redirect:/reservation/data";
    }

    @GetMapping("/data")
    public String getReservationDataForm(
            @ModelAttribute("reservationDto") ReservationDto reservationDto,
            Model model
    ) {

        List<Price> prices = priceService.getPrices();
        model.addAttribute("priceList", prices);
        return "reservation-data";
    }


    @PostMapping("/data")
    public String fillReservationData(
            @Valid @ModelAttribute("reservationDto") ReservationDto reservationDto,
            BindingResult theBindingResult,
            Model model
    ) {

        for(Ticket ticket : reservationDto.getTickets()) {
            if(ticket.getTicketType() == null) {
                List<Price> prices = priceService.getPrices();
                model.addAttribute("priceList", prices);
                model.addAttribute("errorMessage", "Nie wybrano typu bietów");
                return "reservation-data";
            }
        }

        if(theBindingResult.hasErrors()) {
            List<Price> prices = priceService.getPrices();
            model.addAttribute("priceList", prices);
            return "reservation-data";
        }

        if(!reservationDto.getClientAddressEmail().equals(reservationDto.getConfirmedClientAddressEmail())) {
            List<Price> prices = priceService.getPrices();
            model.addAttribute("priceList", prices);
            model.addAttribute("emailErrorMessage", "Adresy e-mail różnią się");
            return "reservation-data";
        }

        List<Ticket> tickets = reservationDto.getTickets();

        double totalPrice;
        List<Price> priceList = priceService.getPrices();

        tickets.forEach(ticket -> {
            priceList.stream()
                    .filter(price -> price.getType().equalsIgnoreCase(ticket.getTicketType()))
                    .findFirst()
                    .ifPresent(price -> {
                        ticket.setPrice(price.getPriceValue());
                    });
        });

        totalPrice = tickets.stream().mapToDouble(Ticket::getPrice).sum();
        reservationDto.setTotalPrice(totalPrice);

        return "redirect:/reservation/summary";
    }


    @GetMapping("/summary")
    public String getSummaryReservationPage(@ModelAttribute("reservationDto") ReservationDto reservationDto) {

        return "reservation-summary";
    }


    @PostMapping("/summary")
    public String confirmReservation(@ModelAttribute("reservationDto") ReservationDto reservationDto) {

        Reservation reservation = new Reservation();
        reservation.setReservationDate(LocalDateTime.now());

        reservation.setClientName(reservationDto.getClientName());
        reservation.setClientSurname(reservationDto.getClientSurname());
        reservation.setClientAddressEmail(reservationDto.getClientAddressEmail());
        reservation.setClientPhoneNumber(reservationDto.getClientPhoneNumber());
        reservation.setPrice(reservationDto.getTotalPrice());

        List<Ticket> tickets = reservationDto.getTickets();
        tickets.forEach(ticket -> ticket.setReservation(reservation));
        reservation.setTickets(reservationDto.getTickets());

        reservationService.save(reservation);

        return "redirect:/mainpage";
    }

}