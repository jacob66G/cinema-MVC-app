package com.example.Cinema.controller;

import com.example.Cinema.mapper.ClientMapper;
import com.example.Cinema.model.dto.ClientDto;
import com.example.Cinema.repository.UserRepository;
import com.example.Cinema.service.Validators.UserValidationService;
import jakarta.validation.Valid;
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

    private final UserValidationService userValidationService;
    private final UserRepository userRepository;
    private final ClientMapper clientMapper;

    public RegistrationController(UserValidationService userValidationService, UserRepository userRepository, ClientMapper clientMapper) {
        this.userValidationService = userValidationService;
        this.userRepository = userRepository;
        this.clientMapper = clientMapper;
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

        if(!userValidationService.isPasswordValid(clientDto.getPassword(), clientDto.getConfirmPassword())) {
            model.addAttribute("errorMessage", "Hasła się różnią");
            return "registration";
        }

        if(userValidationService.findByUserEmail(clientDto.getEmail())) {
            model.addAttribute("errorMessage", "Użytkownik o tym adresie email już istnieje");
            return "registration";
        }

        if(userValidationService.findByUserPhone(clientDto.getPhone())) {
            model.addAttribute("errorMessage", "Użytkownik z tym numerem telefonu już istnieje");
            return "registration";
        }

        userRepository.save(clientMapper.fromDto(clientDto));

        return "redirect:/mainpage";
    }
}
