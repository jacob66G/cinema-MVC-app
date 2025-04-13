package com.example.Cinema.controller;

import com.example.Cinema.exception.ValidationException;
import com.example.Cinema.model.Reservation;
import com.example.Cinema.model.Ticket;
import com.example.Cinema.model.User;
import com.example.Cinema.model.dto.*;
import com.example.Cinema.service.PDFGenerator.PdfService;
import com.example.Cinema.service.PriceService;
import com.example.Cinema.service.UserService;
import com.example.Cinema.service.ReservationService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {

    private final ReservationService reservationService;
    private final PdfService pdfService;
    private final UserService userService;

    public UserController(
            ReservationService reservationService, PdfService pdfService,
            UserService userService
    ) {
        this.reservationService = reservationService;
        this.pdfService = pdfService;
        this.userService = userService;
    }


    @ModelAttribute("authenticatedUser")
    public User authenticatedUser(@AuthenticationPrincipal UserDetails userDetails) {
        return userService.getByName(userDetails.getUsername());
    }

    @ModelAttribute
    public void addCommonAttributes(Model model, @ModelAttribute("authenticatedUser") User user) {
        model.addAttribute("personalData", new PersonalDataDto(user.getName(), user.getSurname(), user.getPhone()));
        model.addAttribute("passwordChange", new PasswordChangeDto());
    }

    @GetMapping("/reservations")
    public String getClientReservation(@ModelAttribute("authenticatedUser") User user, Model model) {

        List<Reservation> reservations = reservationService.getReservations(user);

        if(reservations.isEmpty()) {
            model.addAttribute("message", "Brak rezerwacji");
        }
        else {
            model.addAttribute("reservations", reservations);
        }

        return "client-page";
    }

    @GetMapping("/reservation/details")
    public String getClientReservationDetails(@RequestParam Long id, Model model) {
        Reservation reservation = reservationService.getReservationDetails(id);

        model.addAttribute("reservation", reservation);
        return "client-reservation";
    }

    @GetMapping("/reservation/cancel")
    public String cancelReservation(@RequestParam Long id) {
        reservationService.delete(id);

        return "redirect:/user/reservations";
    }


    @GetMapping("/settings")
    public String getClientSettings() {
        return "client-settings";
    }

    @PostMapping("/settings/change/personal-data")
    public String editClientData(
            @Valid @ModelAttribute("personalData") PersonalDataDto personalData,
            BindingResult bindingResult,
            @ModelAttribute("authenticatedUser") User user,
            Model model
    ) {

        if (bindingResult.hasErrors()) {
            return "client-settings";
        }

        try {
            userService.changePersonalData(user, personalData);
            return "redirect:/user/settings";

        } catch (ValidationException e) {
            model.addAttribute("errorMessagePhone", e.getMessage());
            return "client-settings";
        }
    }

    @PostMapping("/settings/change/password")
    public String editClientPassword(
            @Valid @ModelAttribute("passwordChange") PasswordChangeDto passwordChangeDto,
            BindingResult bindingResult,
            @ModelAttribute("authenticatedUser") User user,
            Model model
    ) {

        if (bindingResult.hasErrors()) {
            return "client-settings";
        }

        try {
            userService.changePassword(user, passwordChangeDto);
            return "redirect:/user/settings";

        } catch (ValidationException e) {
            model.addAttribute("errorMessagePassword", e.getMessage());
            return "client-settings";
        }
    }

    @GetMapping("/reservation/{id}/pdf")
    public ResponseEntity<byte[]> generateReservationPDF(@PathVariable Long id) {
        Reservation reservation = reservationService.getReservationById(id);

        Map<String, Object> data = new HashMap<>();
        data.put("reservation", reservation);
        data.put("price", reservation.getTickets().stream().mapToDouble(Ticket::getPrice));

        byte[] pdf;
        try {
            pdf = pdfService.generatePdf(data, "reservation-pdf");
        }  catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.inline().filename("rezerwacja.pdf").build());

        return new ResponseEntity<>(pdf, headers, HttpStatus.OK);
    }
}
