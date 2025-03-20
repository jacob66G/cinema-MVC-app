package com.example.Cinema.controller;

import com.example.Cinema.mapper.ProgrammeMapper;
import com.example.Cinema.mapper.ReservationMapper;
import com.example.Cinema.mapper.SeatMapper;
import com.example.Cinema.exception.ProgrammeNotFoundException;
import com.example.Cinema.service.PDFGenerator.PdfGenerator;
import com.example.Cinema.model.*;
import com.example.Cinema.model.dto.ProgrammeDto;
import com.example.Cinema.model.dto.ReservationDto;
import com.example.Cinema.model.dto.SeatDto;
import com.example.Cinema.model.dto.SeatListDto;
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
    private final ProgrammeMapper programmeMapper;
    private final ReservationMapper reservationMapper;
    private final SeatMapper seatMapper;
    private final PdfGenerator pdfGenerator;

    @Autowired
    public ReservationController(
            ReservationService reservationService,
            ProgrammeService programmeService,
            TicketService ticketService,
            PriceService priceService,
            ReservationValidationService reservationValidationService, ProgrammeMapper programmeMapper, ReservationMapper reservationMapper, SeatMapper seatMapper, PdfGenerator pdfGenerator
    ) {
        this.reservationService = reservationService;
        this.programmeService = programmeService;
        this.ticketService = ticketService;
        this.priceService = priceService;
        this.reservationValidationService = reservationValidationService;
        this.programmeMapper = programmeMapper;
        this.reservationMapper = reservationMapper;
        this.seatMapper = seatMapper;
        this.pdfGenerator = pdfGenerator;
    }

    @ModelAttribute("priceList")
    public List<Price> priceList() {
        return priceService.getPrices();
    }

    @GetMapping()
    public String getReservationPage(@RequestParam(required = false) Long id, Model model) {
        Programme programme = programmeService.getProgrammeById(id).orElseThrow(() -> new ProgrammeNotFoundException(id));

        List<Seat> bookedSeats = ticketService.getBookedSeats(programme);
        List<Seat> seats = programme.getCinemaHall().getSeats();

        List<SeatDto> seatsDto = seats.stream().map(seat -> {
            boolean bookedSeat = bookedSeats.contains(seat);
            return seatMapper.toDto(seat, bookedSeat);
        }).toList();

        ProgrammeDto programmeDto = programmeMapper.toDto(programme);

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
            return "redirect:/reservation?id=" + programme.getId();
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

        return "redirect:/reservation/summary";
    }


    @GetMapping("/summary")
    public String getSummaryReservationPage(@ModelAttribute("reservationDto") ReservationDto reservationDto, Model model) {

        model.addAttribute("totalPrice", priceService.calculateTotalPrice(reservationDto.getTickets()));
        return "reservation-summary";
    }


    @PostMapping("/summary")
    public String confirmReservation(@ModelAttribute("reservationDto") ReservationDto reservationDto, HttpServletResponse response) throws DocumentException, IOException {
        Reservation reservation = reservationMapper.fromDto(reservationDto);
        reservationService.save(reservation);

//        response.setContentType("application/pdf");
//
//        String headerKey = "Content-Disposition";
//        String headerValue = "inline; filename=reservation.pdf";
//        response.setHeader(headerKey, headerValue);
//        this.pdfGenerator.export(response, reservation);

        return "redirect:/mainpage";
    }
}