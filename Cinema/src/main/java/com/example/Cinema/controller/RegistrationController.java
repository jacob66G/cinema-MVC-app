package com.example.Cinema.controller;

import com.example.Cinema.mapper.ClientMapper;
import com.example.Cinema.model.dto.ClientDto;
import com.example.Cinema.repository.UserRepository;
import com.example.Cinema.service.UserService;
import com.example.Cinema.service.Validators.UserValidationService;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/registration")
public class RegistrationController {

    private final UserService userService;

    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String registration(Model model) {
        model.addAttribute("client", new ClientDto());
        return "registration";
    }

    @PostMapping
    public String registration(@Valid @ModelAttribute("client") ClientDto clientDto, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            return "registration";
        }

        try {
            userService.save(clientDto);
            return "redirect:/mainpage";

        } catch (ValidationException e) {
            model.addAttribute("errorMessage", e);
            return "registration";
        }
    }
}
