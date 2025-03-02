package com.example.Cinema.controller;

import com.example.Cinema.config.PdfGenerator;
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
import com.example.Cinema.service.Validators.ReservationValidationService;
import com.itextpdf.text.DocumentException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.text.DateFormat;
import java.time.LocalDateTime;
import java.util.*;


@Controller
@RequestMapping("/reservation")
@SessionAttributes({"selectedProgramme","programmeDto", "reservationDto", "priceList"})
public class ReservationController {

    private final ReservationService reservationService;
    private final ProgrammeService programmeService;
    private final TicketService ticketService;
    private final PriceService priceService;
    private final ReservationValidationService reservationValidationService;
    private final PdfGenerator pdfGenerator;

    @Autowired
    public ReservationController(
            ReservationService reservationService,
            ProgrammeService programmeService,
            TicketService ticketService,
            PriceService priceService,
            ReservationValidationService reservationValidationService, PdfGenerator pdfGenerator
    ) {
        this.reservationService = reservationService;
        this.programmeService = programmeService;
        this.ticketService = ticketService;
        this.priceService = priceService;
        this.reservationValidationService = reservationValidationService;
        this.pdfGenerator = pdfGenerator;
    }

    @ModelAttribute("priceList")
    public List<Price> priceList() {
        return priceService.getPrices();
    }

    @GetMapping()
    public String getReservationPage(@RequestParam(required = false) Long id, Model model) {
        Programme programme = programmeService.getProgrammeById(id).orElseThrow();

        List<Seat> bookedSeats = ticketService.getBookedSeats(programme);
        List<Seat> seats = programme.getCinemaHall().getSeats();

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

        Movie movie = programme.getMovie();

        ProgrammeDto programmeDto = new ProgrammeDto();
        programmeDto.setId(id);
        programmeDto.setDate(programme.getDate());
        programmeDto.setTime(programme.getTime());
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

        try {
            ReservationDto reservationDto = reservationService.createReservationDto(programme, seatListDto);
            model.addAttribute("reservationDto", reservationDto);
            return "redirect:/reservation/data";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/reservation?id=" + programme.getIdprogramme();
        }
    }

    @GetMapping("/data")
    public String getReservationDataForm(
            @ModelAttribute("reservationDto") ReservationDto reservationDto,
            @ModelAttribute("priceList") List<Price> priceList
    ) {

        return "reservation-data";
    }


    @PostMapping("/data")
    public String fillReservationData(
            @Valid @ModelAttribute("reservationDto") ReservationDto reservationDto,
            BindingResult theBindingResult,
            @ModelAttribute("priceList") List<Price> priceList,
            Model model
    ) {

        List<Ticket> tickets = reservationDto.getTickets();

        if(!reservationValidationService.areTicketsValid(tickets)) {
            model.addAttribute("errorMessage", "Nie wybrano typu bietów");
            return "reservation-data";
        }

        if(!reservationValidationService.isEmailValid(reservationDto.getClientAddressEmail(), reservationDto.getConfirmedClientAddressEmail())) {
            model.addAttribute("emailErrorMessage", "Adresy e-mail różnią się");
            return "reservation-data";
        }

        if(theBindingResult.hasErrors()) {
            return "reservation-data";
        }

        reservationDto.setTotalPrice(priceService.calculateTotalPrice(tickets));

        return "redirect:/reservation/summary";
    }


    @GetMapping("/summary")
    public String getSummaryReservationPage(@ModelAttribute("reservationDto") ReservationDto reservationDto) {

        return "reservation-summary";
    }


    @PostMapping("/summary")
    public String confirmReservation(@ModelAttribute("reservationDto") ReservationDto reservationDto, HttpServletResponse response) throws DocumentException, IOException {

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

        response.setContentType("application/pdf");

        String headerKey = "Content-Disposition";
        String headerValue = "inline; filename=reservation.pdf";
        response.setHeader(headerKey, headerValue);

//        this.pdfGenerator.export(response, reservation);

        return "redirect:/mainpage";
    }

}