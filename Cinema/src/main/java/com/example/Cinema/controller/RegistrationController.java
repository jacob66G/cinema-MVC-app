package com.example.Cinema.controller;

import com.example.Cinema.mapper.ClientMapper;
import com.example.Cinema.model.dto.ClientDto;
import com.example.Cinema.repository.UserRepository;
import com.example.Cinema.service.Validators.ClientValidationService;
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

    private final ClientValidationService clientValidationService;
    private final UserRepository userRepository;
    private final ClientMapper clientMapper;

    public RegistrationController(ClientValidationService clientValidationService, UserRepository userRepository, ClientMapper clientMapper) {
        this.clientValidationService = clientValidationService;
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

        if(!clientValidationService.isPasswordValid(clientDto.getPassword(), clientDto.getConfirmPassword())) {
            model.addAttribute("errorMessage", "Hasła się różnią");
            return "registration";
        }

//        if(userRepository.findByUserEmail()) {
//
//        }


        userRepository.save(clientMapper.fromDto(clientDto));

        return "redirect:/mainpage";
    }
}
