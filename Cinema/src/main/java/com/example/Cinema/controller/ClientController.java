package com.example.Cinema.controller;

import com.example.Cinema.model.User;
import com.example.Cinema.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class ClientController {

    private final UserRepository userRepository;

    public ClientController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping()
    public String getClientPage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userRepository.findByUserName(username);
        model.addAttribute("user", user);

        return "client-page";
    }
}
