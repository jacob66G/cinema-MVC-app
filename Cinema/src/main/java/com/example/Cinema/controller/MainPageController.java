package com.example.Cinema.controller;

import com.example.Cinema.model.InformationModule;
import com.example.Cinema.repository.InformationModuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/mainpage")
public class MainPageController {
    private final InformationModuleRepository informationModuleRepository;

    @Autowired
    public MainPageController(InformationModuleRepository informationModuleRepository) {
        this.informationModuleRepository = informationModuleRepository;
    }

    @GetMapping()
    public String mainPage(Model model){

        InformationModule defaultModule = new InformationModule("", "", "", "", "");

        model.addAttribute("newsModule", informationModuleRepository.findByType("news").orElse(defaultModule));
        model.addAttribute("premiereModule", informationModuleRepository.findByType("premiere").orElse(defaultModule));
        model.addAttribute("presentModule", informationModuleRepository.findByType("present").orElse(defaultModule));
        model.addAttribute("present2Module", informationModuleRepository.findByType("present2").orElse(defaultModule));

        return "main-page";
    }
}
