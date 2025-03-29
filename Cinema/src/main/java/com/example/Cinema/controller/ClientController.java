package com.example.Cinema.controller;

import com.example.Cinema.mapper.ReservationMapper;
import com.example.Cinema.model.Reservation;
import com.example.Cinema.model.User;
import com.example.Cinema.model.dto.*;
import com.example.Cinema.repository.UserRepository;
import com.example.Cinema.service.UserService;
import com.example.Cinema.service.ReservationService;
import com.example.Cinema.service.Validators.UserValidationService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequestMapping("/user")
@SessionAttributes("user")
public class ClientController {

    private final ReservationMapper reservationMapper;
    private final ReservationService reservationService;
    private final UserValidationService userValidationService;
    private final UserService userService;

    public ClientController(UserRepository userRepository,
                            ReservationMapper reservationMapper,
                            ReservationService reservationService,
                            UserValidationService userValidationService,
                            UserService userService) {

        this.reservationMapper = reservationMapper;
        this.reservationService = reservationService;
        this.userValidationService = userValidationService;
        this.userService = userService;
    }

    @ModelAttribute("user")
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        return userService.findByUserName(username);
    }

    @GetMapping("/reservations")
    public String getClientReservations(@ModelAttribute User user, Model model) {

        List<ReservationDto> reservationDtos = reservationService.getReservations(user).stream()
                .map(reservationMapper::toDto).toList();

        if(reservationDtos.isEmpty()) {
            model.addAttribute("message", "Brak rezerwacji");
        }
        else {
            model.addAttribute("reservations", reservationDtos);
        }

        return "client-page";
    }

    @GetMapping("/reservation/details")
    public String getClientReservationDetails(@RequestParam Long id, Model model) {
        Reservation reservation = reservationService.getReservations(id);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        model.addAttribute("formatTime", reservation.getReservationDate().format(formatter));
        model.addAttribute("reservation", reservation);
        return "client-reservation";
    }

    @GetMapping("/reservation/cancel")
    public String cancelReservation(@RequestParam Long id, Model model) {
        reservationService.delete(id);

        return "redirect:/user/reservations";
    }

    @GetMapping("/settings")
    public String getClientSettings(@ModelAttribute User user,  Model model) {

        model.addAttribute("peronalData", new PersonalDataDto(user.getName(), user.getSurname(), user.getPhone()));
        model.addAttribute("emailChange", new EmailChangeDto());
        model.addAttribute("passwordChange", new PasswordChangeDto());
        return "client-settings";
    }

    @PostMapping("/settings/change/personal-data")
    public String editClientData(@Valid @ModelAttribute("personalData") PersonalDataDto personalDataDto,
                                 BindingResult bindingResult, @ModelAttribute User user, Model model) {
        if (bindingResult.hasErrors()) {
            return "client-settings";
        }

        userService.changeClientPersonalData(user, personalDataDto);
        return "redirect:/user/settings";
    }

    @PostMapping("/settings/change/email")
    public String editClientEmail(@Valid @ModelAttribute("emailChange") EmailChangeDto emailChangeDto,
                                  BindingResult bindingResult,@ModelAttribute User user, Model model) {
        if (bindingResult.hasErrors()) {
            return "client-settings";
        }

        if (!userValidationService.isEmailValid(emailChangeDto.getEmail(), emailChangeDto.getConfirmEmail())) {
            return "client-settings";
        }

        userService.changeClientEmail(user, emailChangeDto);
        return "redirect:/user/settings";
    }

    @PostMapping("/settings/change/password")
    public String editClientPassword(@Valid @ModelAttribute("passwordChange") PasswordChangeDto passwordChangeDto,
                                     BindingResult bindingResult, @ModelAttribute User user, Model model) {
        if (bindingResult.hasErrors()) {
            return "client-settings";
        }

        if (!userValidationService.isOldPasswordValid(user, passwordChangeDto.getCurrentPassword())) {
            return "client-settings";
        }

        if (!userValidationService.isPasswordValid(passwordChangeDto.getNewPassword(), passwordChangeDto.getConfirmPassword())) {
            return "client-settings";
        }

        userService.changeClientPassword(user, passwordChangeDto);
        return "redirect:/user/settings";
    }
}
