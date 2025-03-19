package com.example.Cinema.controller;

import com.example.Cinema.mapper.MovieMapper;
import com.example.Cinema.model.dto.MovieDto;
import com.example.Cinema.model.Programme;
import com.example.Cinema.service.ProgrammeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.*;

@Controller
@RequestMapping("/programme")
public class ProgrammeController {

    private final ProgrammeService programmeService;
    private final MovieMapper movieMapper;

    @Autowired
    public ProgrammeController(ProgrammeService programmeService, MovieMapper movieMapper) {
        this.programmeService = programmeService;
        this.movieMapper = movieMapper;
    }

    @GetMapping
    public String showProgramme(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate selectedDate,
            Model model
    ){

        if(selectedDate == null){
            selectedDate = LocalDate.now();
        }

        List<Programme> programmeList = programmeService.getProgrammes(selectedDate);
        programmeList.sort(Comparator.comparing(Programme::getTime));

        List<MovieDto> movies = programmeList.stream()
                .map(Programme::getMovie)
                .distinct()
                .map(movieMapper::toDto).toList();

        model.addAttribute("movies", movies);
        model.addAttribute("selectedDate", selectedDate);
        model.addAttribute("programmeList", programmeList);

        return "programme";
    }

}
