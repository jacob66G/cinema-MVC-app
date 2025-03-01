package com.example.Cinema.controller;

import com.example.Cinema.model.Dto.MovieDto;
import com.example.Cinema.model.Dto.ProgrammeDto;
import com.example.Cinema.model.Movie;
import com.example.Cinema.model.Programme;
import com.example.Cinema.service.MovieService;
import com.example.Cinema.service.ProgrammeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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

    @GetMapping
    public String showProgramme(@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate selectedDate, Model model){

        if(selectedDate == null){
            selectedDate = LocalDate.now();
        }

        List<Programme> programmeList = programmeService.getProgrammes(selectedDate, null);
        programmeList.sort(Comparator.comparing(Programme::getTime));

        List<MovieDto> movies = programmeList.stream()
                .map(Programme::getMovie)
                .distinct()
                .map(movie -> {
                    MovieDto movieDto = new MovieDto();
                    movieDto.setIdmovie(movie.getIdmovie());
                    movieDto.setTitle(movie.getTitle());
                    movieDto.setDescription(movie.getDescription());

                    String base64Image = Base64.getEncoder().encodeToString(movie.getImageData());
                    movieDto.setBase64Image(base64Image);
                    return movieDto;
                }).toList();

        model.addAttribute("movies", movies);
        model.addAttribute("selectedDate", selectedDate);
        model.addAttribute("programmeList", programmeList);

        return "programme";
    }

}
