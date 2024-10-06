package com.example.Cinema.controller;

import com.example.Cinema.model.Movie;
import com.example.Cinema.model.Programme;
import com.example.Cinema.service.MovieService;
import com.example.Cinema.service.ProgrammeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Controller
@RequestMapping("/admin/edit-programme/{id}")
public class AdminProgrammeController {

    private final ProgrammeService programmeService;
    private final MovieService movieService;

    @Autowired
    public AdminProgrammeController(ProgrammeService programmeService, MovieService movieService) {
        this.programmeService = programmeService;
        this.movieService = movieService;
    }

    @GetMapping()
    public String getProgrammePage(@PathVariable Long id, Model model){
        LocalDate date = LocalDate.now();
        return prepareProgrammePage(id, date, model);
    }

    @PostMapping()
    public String editProgramme(
            @PathVariable Long id,
            @RequestParam(required = false) LocalDate date,
            @RequestParam(required = false) LocalTime time,
            @RequestParam(required = false) String hall,
            @RequestParam(required = false) LocalDate programmeDate,
            @RequestParam String action,
            Model model) {

        Optional<Movie> movie = movieService.findById(id);

        if("add".equals(action)){
            String errorMessage = programmeService.handleAddProgramme(movie.get(), date, time, hall);
            if(errorMessage != null){
                model.addAttribute("errorMessage", errorMessage);
                model.addAttribute("movie", movie.get());
                return "adminview/edit-movie-programme";
            }
        } else if ("delete".equals(action)) {
            String errorMessage = programmeService.handleDeleteProgramme(id, date, time, hall);
            if(errorMessage != null){
                model.addAttribute("errorMessage", errorMessage);
                model.addAttribute("movie", movie.get());
                return "adminview/edit-movie-programme";
            }
        }

        return prepareProgrammePage(id, programmeDate, model);
    }

    public String prepareProgrammePage(Long movieId, LocalDate programmeDate, Model model){
        Optional<Movie> movie = movieService.findById(movieId);

        if(programmeDate == null){
            programmeDate = LocalDate.now();
        }
        List<Programme> programmesForSelectedDate = programmeService.findByDate(programmeDate);

        Map<Movie, List<Programme>> programmeListGroupedByMovie = programmeService.getProgrammeListGroupedByMovie(programmesForSelectedDate);

        Map<Movie, Set<String>> hallsForMovie = programmeService.getHallsForMovie(programmeListGroupedByMovie);

        model.addAttribute("movie", movie.get());
        model.addAttribute("programmeListGroupedByMovie", programmeListGroupedByMovie);
        model.addAttribute("hallNames", hallsForMovie);
        model.addAttribute("selectedDate", programmeDate);

        return "adminview/edit-movie-programme";
    }
}
