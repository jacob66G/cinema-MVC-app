package com.example.Cinema.controller;

import com.example.Cinema.Mapper.ShowcasesMapper;
import com.example.Cinema.model.Dto.ShowcaseDto;
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
    private final ShowcasesMapper showcasesMapper;

    public MainPageController(ShowcaseService showcaseService, ShowcasesMapper showcasesMapper) {
        this.showcaseService = showcaseService;
        this.showcasesMapper = showcasesMapper;
    }

    @GetMapping()
    public String MainPage(Model model) {
        List<Showcase> showcases = showcaseService.getShowcases();
        List<ShowcaseDto> showcaseDtos = showcases.stream().map(showcasesMapper::toDto).toList();

        model.addAttribute("showcases", showcaseDtos);
        return "main-page";
    }
}
