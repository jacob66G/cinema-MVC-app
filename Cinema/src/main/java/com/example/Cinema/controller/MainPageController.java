package com.example.Cinema.controller;

import com.example.Cinema.mapper.MovieMapper;
import com.example.Cinema.model.dto.MovieDto;
import com.example.Cinema.model.Movie;
import com.example.Cinema.repository.MovieRepository;
import com.example.Cinema.service.MovieService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/mainpage")
public class MainPageController {

    private final MovieService movieService;

    public MainPageController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping()
    public String MainPage(Model model) {
        List<MovieDto> moviesDto = movieService.getAllMoviesDto();
        model.addAttribute("movies", moviesDto);
        return "main-page";
    }
}
