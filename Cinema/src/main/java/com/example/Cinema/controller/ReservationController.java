package com.example.Cinema.controller;

import com.example.Cinema.exception.ValidationException;
import com.example.Cinema.model.dto.*;
import com.example.Cinema.model.enums.TicketCategory;
import com.example.Cinema.service.*;
import com.example.Cinema.model.*;
import com.example.Cinema.service.Validators.ReservationValidationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;

@Controller
@RequestMapping("/reservation")
@SessionAttributes("reservationDto")
public class ReservationController {

    private final ReservationService reservationService;
    private final ProgrammeService programmeService;
    private final SeatService seatService;
    private final ReservationValidationService reservationValidationService;
    private final UserService userService;

    @Autowired
    public ReservationController(
            ReservationService reservationService,
            ProgrammeService programmeService,
            SeatService seatService,
            ReservationValidationService reservationValidationService,
            UserService userService
    ) {
        this.reservationService = reservationService;
        this.programmeService = programmeService;
        this.seatService = seatService;
        this.reservationValidationService = reservationValidationService;
        this.userService = userService;
    }


    @ModelAttribute("reservationDto")
    public ReservationDto addAttributes() {
       return new ReservationDto();
    }


    @GetMapping()
    public String getReservationPage(
            @RequestParam(required = false) Long id,
            @ModelAttribute("reservationDto") ReservationDto reservationDto,
            Model model
    ) {
        List<SeatDto> seatsDto = seatService.getSeatsWithBookingStatus(id);
        ProgrammeDto programmeDto = programmeService.getProgrammeDtoById(id);

        reservationDto.setProgramme(programmeDto);

        model.addAttribute("programmeDto", programmeDto);
        model.addAttribute("seatsForm", new SeatListDto(seatsDto));
        model.addAttribute("seats", seatsDto);

        return "reservation";
    }


    @PostMapping()
    public String processSeats(
            @ModelAttribute("seatsForm") SeatListDto seatListDto,
            @ModelAttribute("reservationDto") ReservationDto reservationDto,
            RedirectAttributes redirectAttributes,
            Model model
    ) {
        if(seatListDto.getSeats().stream().noneMatch(SeatDto::isChosen)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Nie wybrano miejsca");
            return "redirect:/reservation?id=" + reservationDto.getProgramme().getId();
        }

        reservationService.assignTicketsToReservation(seatListDto, reservationDto);

        model.addAttribute("reservationDto", reservationDto);
        return "redirect:/reservation/data";
    }


    @GetMapping("/data")
    public String getReservationDataForm(
            @ModelAttribute("reservationDto") ReservationDto reservationDto,
            Model model
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getByName(authentication.getName());

        if(user != null) {
            reservationDto.setUser(user);
        }

        model.addAttribute("TicketTypes", getTicketTypes());
        return "reservation-data";
    }


    @PostMapping("/data")
    public String fillReservationData (
            @Valid @ModelAttribute("reservationDto") ReservationDto reservationDto,
            BindingResult theBindingResult,
            Model model
    ) {

        if(theBindingResult.hasErrors()) {
            model.addAttribute("TicketTypes", getTicketTypes());
            return "reservation-data";
        }

        try {
            reservationValidationService.validate(reservationDto);
            reservationService.applyPrices(reservationDto.getTickets());

        } catch (ValidationException e) {
            model.addAttribute("TicketTypes", getTicketTypes());
            model.addAttribute("errorMessage", e.getMessage());
            return "reservation-data";
        }

        return "redirect:/reservation/summary";
    }

    @GetMapping("/summary")
    public String getSummaryReservationPage(
            @ModelAttribute("reservationDto") ReservationDto reservationDto,
            Model model
    ) {
        model.addAttribute("totalPrice", reservationService.getTotalPrice(reservationDto));
        return "reservation-summary";
    }


    @PostMapping("/summary")
    public String confirmReservation(@ModelAttribute("reservationDto") ReservationDto reservationDto) {
        reservationService.createReservation(reservationDto);
        return "redirect:/mainpage";
    }

    private static List<String> getTicketTypes() {
        List<String> types = Arrays.stream(TicketCategory.values())
                .map(Enum::name)
                .toList();
        return types;
    }
}