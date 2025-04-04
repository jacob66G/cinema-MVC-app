package com.example.Cinema.controller;

import com.example.Cinema.exception.ValidationException;
import com.example.Cinema.mapper.ProgrammeMapper;
import com.example.Cinema.repository.UserRepository;
import com.example.Cinema.service.*;
import com.example.Cinema.model.*;
import com.example.Cinema.model.dto.ProgrammeDto;
import com.example.Cinema.model.dto.ReservationDto;
import com.example.Cinema.model.dto.SeatDto;
import com.example.Cinema.model.dto.SeatListDto;
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
@SessionAttributes({"selectedProgramme","programmeDto", "reservationDto", "priceList"})
public class ReservationController {

    private final ReservationService reservationService;
    private final ProgrammeService programmeService;
    private final PriceService priceService;
    private final SeatService seatService;
    private final UserRepository userRepository;
    private final ProgrammeMapper programmeMapper;
    private final ReservationValidationService reservationValidationService;

    @Autowired
    public ReservationController(
            ReservationService reservationService,
            ProgrammeService programmeService,
            PriceService priceService, SeatService seatService,
            UserRepository userRepository, ProgrammeMapper programmeMapper, ReservationValidationService reservationValidationService
    ) {
        this.reservationService = reservationService;
        this.programmeService = programmeService;
        this.priceService = priceService;
        this.seatService = seatService;
        this.userRepository = userRepository;
        this.programmeMapper = programmeMapper;
        this.reservationValidationService = reservationValidationService;
    }

    @ModelAttribute("priceList")
    public List<Price> priceList() {
        return priceService.getPrices();
    }

    @GetMapping()
    public String getReservationPage(@RequestParam(required = false) Long id, Model model) {
        Programme programme = programmeService.getProgrammeById(id);
        List<SeatDto> seatsDto = seatService.getSeatsWithBookingStatus(programme);

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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUserName(authentication.getName());

        if(user != null) {
            reservationDto.setClientName(user.getName());
            reservationDto.setClientSurname(user.getSurname());
            reservationDto.setClientPhoneNumber(user.getPhone());
            reservationDto.setClientAddressEmail(user.getUserName());
            reservationDto.setConfirmedClientAddressEmail(user.getUserName());
            reservationDto.setUser(user);
        }

        return "reservation-data";
    }


    @PostMapping("/data")
    public String fillReservationData (
            @Valid @ModelAttribute("reservationDto") ReservationDto reservationDto,
            BindingResult theBindingResult,
            @ModelAttribute("priceList") List<Price> priceList,
            Model model
    ) {

        List<Ticket> tickets = reservationDto.getTickets();

        try {
            reservationValidationService.areTicketsValid(tickets);
            reservationValidationService.isEmailValid(reservationDto.getClientAddressEmail(), reservationDto.getConfirmedClientAddressEmail());

        } catch (ValidationException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "reservation-data";
        }

//        if(!reservationValidationService.areTicketsValid(tickets)) {
//            model.addAttribute("errorMessage", "Nie wybrano typu bietów");
//            return "reservation-data";
//        }
//
//        if(!reservationValidationService.isEmailValid(reservationDto.getClientAddressEmail(), reservationDto.getConfirmedClientAddressEmail())) {
//            model.addAttribute("emailErrorMessage", "Adresy e-mail różnią się");
//            return "reservation-data";
//        }

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
    public String confirmReservation(@ModelAttribute("reservationDto") ReservationDto reservationDto) {
        reservationService.save(reservationDto);
        return "redirect:/mainpage";
    }
}