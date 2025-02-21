package com.example.Cinema.controller;

import com.example.Cinema.model.Dto.ShowcaseDto;
import com.example.Cinema.model.Showcase;
import com.example.Cinema.service.ShowcaseService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Base64;
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
        List<Showcase> showcases = showcaseService.getShowcases();

        List<ShowcaseDto> showcaseDtos = showcases.stream().map(showcase -> {
            String base64Image = Base64.getEncoder().encodeToString(showcase.getImageData());
            return new ShowcaseDto(showcase.getIdShowcase(), showcase.getType(), showcase.getTitle(), base64Image);
        }).toList();

        model.addAttribute("showcases", showcaseDtos);
        return "main-page";
    }
}
