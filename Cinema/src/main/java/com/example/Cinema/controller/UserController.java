package com.example.Cinema.controller;

import com.example.Cinema.exception.ValidationException;
import com.example.Cinema.model.Reservation;
import com.example.Cinema.model.User;
import com.example.Cinema.model.dto.*;
import com.example.Cinema.service.UserService;
import com.example.Cinema.service.ReservationService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/user")
@SessionAttributes("user")
public class UserController {

    private final ReservationService reservationService;
    private final UserService userService;

    public UserController(
                            ReservationService reservationService,
                            UserService userService) {

        this.reservationService = reservationService;
        this.userService = userService;
    }

    @ModelAttribute
    public void addCommonAttributes(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByUserName(authentication.getName());
        model.addAttribute("personalData", new PersonalDataDto(user.getName(), user.getSurname(), user.getPhone()));
        model.addAttribute("emailChange", new EmailChangeDto());
        model.addAttribute("passwordChange", new PasswordChangeDto());
    }

    @GetMapping("/reservations")
    public String getClientReservation(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByUserName(authentication.getName());

        List<ReservationDto> reservationsDto = reservationService.getReservationsDto(user);

        if(reservationsDto.isEmpty()) {
            model.addAttribute("message", "Brak rezerwacji");
        }
        else {
            model.addAttribute("reservations", reservationsDto);
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
    public String cancelReservation(@RequestParam Long id, Model model) {
        reservationService.delete(id);

        return "redirect:/user/reservations";
    }

    @GetMapping("/settings")
    public String getClientSettings() {
        return "client-settings";
    }

    @PostMapping("/settings/change/personal-data")
    public String editClientData(@Valid @ModelAttribute("personalData") PersonalDataDto personalData,
                                 BindingResult bindingResult, @ModelAttribute User user, Model model) {

        if (bindingResult.hasErrors()) {
            return "client-settings";
        }

        try {
            userService.changeClientPersonalData(user, personalData);
            return "redirect:/user/settings";

        } catch (ValidationException e) {
            model.addAttribute("errorMessagePhone", e.getMessage());
            return "client-settings";
        }
    }

    @PostMapping("/settings/change/email")
    public String editClientEmail(@Valid @ModelAttribute("emailChange") EmailChangeDto emailChange,
                                  BindingResult bindingResult,@ModelAttribute User user, Model model) {

        if (bindingResult.hasErrors()) {
            return "client-settings";
        }

        try {
            userService.changeClientEmail(user, emailChange);
            return "redirect:/user/settings";
        }
        catch (ValidationException e) {
            model.addAttribute("errorMessageEmail", e.getMessage());
            return "client-settings";
        }
    }

    @PostMapping("/settings/change/password")
    public String editClientPassword(@Valid @ModelAttribute("passwordChange") PasswordChangeDto passwordChangeDto,
                                     BindingResult bindingResult, @ModelAttribute User user, Model model) {

        if (bindingResult.hasErrors()) {
            return "client-settings";
        }

        try {
            userService.changeClientPassword(user, passwordChangeDto);
            return "redirect:/user/settings";

        } catch (ValidationException e) {
            model.addAttribute("errorMessagePassword", e.getMessage());
            return "client-settings";
        }
    }
}
