package com.example.Cinema.controller;

import com.example.Cinema.Mapper.MovieMapper;
import com.example.Cinema.model.Dto.MovieDto;
import com.example.Cinema.model.Movie;
import com.example.Cinema.repository.MovieRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/mainpage")
public class MainPageController {
    private final MovieRepository movieRepository;
    private final MovieMapper movieMapper;

    public MainPageController(MovieRepository movieRepository, MovieMapper movieMapper) {
        this.movieRepository = movieRepository;
        this.movieMapper = movieMapper;
    }

    @GetMapping()
    public String MainPage(Model model) {
        List<Movie> movies = movieRepository.findAll();
        List<MovieDto> moviesDtos = movies.stream().map(movieMapper::toDto).toList();
        model.addAttribute("movies", moviesDtos);

        return "main-page";
    }
}
