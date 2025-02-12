package com.example.Cinema.controller;

import com.example.Cinema.model.Showcase;
import com.example.Cinema.service.ShowcaseService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/mainpage")
public class MainPageController {
    private final ShowcaseService showcaseService;

    public MainPageController(ShowcaseService showcaseService) {
        this.showcaseService = showcaseService;
    }

    @GetMapping()
    public String MainPage(Model model) {

        List<Showcase> showcases = showcaseService.getAllShowcases();

        System.out.println(showcases);

        model.addAttribute("showcases", showcases);

        return "main-page";
    }
}
