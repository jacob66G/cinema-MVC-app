package com.example.Cinema.controller;

import com.example.Cinema.model.Movie;
import com.example.Cinema.model.Programme;
import com.example.Cinema.service.MovieService;
import com.example.Cinema.service.ProgrammeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/programme")
public class ProgrammeController {

    private final ProgrammeService programmeService;

    @Autowired
    public ProgrammeController(ProgrammeService programmeService) {
        this.programmeService = programmeService;
    }

//    @GetMapping
//    public String showProgramme(@RequestParam(required = false) LocalDate selectedDate, Model model){
//
//        if(selectedDate == null){
//            selectedDate = LocalDate.now();
//        }
//
//        List<Programme> programmeList = programmeService.findByDate(selectedDate);
//        programmeList.sort(Comparator.comparing(Programme::getTime));
//
//        LocalDate finalSelectedDate = selectedDate;
//        List<Movie> movieList = programmeList.stream()
//                        .filter(programme -> programme.getDate().equals(finalSelectedDate))
//                        .map(Programme::getMovie)
//                        .distinct()
//                        .sorted(Comparator.comparing(Movie::getTitle))
//                        .collect(Collectors.toList());
//
//
//        model.addAttribute("selectedDate", selectedDate);
//        model.addAttribute("programmeList", programmeList);
//        model.addAttribute("movieList", movieList);
//
//        return "programme";
//    }
}
