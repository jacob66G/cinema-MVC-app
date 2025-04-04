package com.example.Cinema.controller;

import com.example.Cinema.exception.ValidationException;
import com.example.Cinema.model.CinemaHall;
import com.example.Cinema.model.dto.ProgrammeDto;
import com.example.Cinema.model.Movie;
import com.example.Cinema.model.Programme;
import com.example.Cinema.service.CinemaHallService;
import com.example.Cinema.service.MovieService;
import com.example.Cinema.service.ProgrammeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;


@Controller
@RequestMapping("/admin/programme")
@SessionAttributes({"cinemaHalls", "movies"})
public class AdminProgrammeController {

    private final ProgrammeService programmeService;
    private final MovieService movieService;
    private final CinemaHallService cinemaHallService;

    @Autowired
    public AdminProgrammeController(
            ProgrammeService programmeService,
            MovieService movieService,
            CinemaHallService cinemaHallService
    ) {
        this.programmeService = programmeService;
        this.movieService = movieService;
        this.cinemaHallService = cinemaHallService;
    }

    @ModelAttribute("cinemaHalls")
    public List<CinemaHall> getCinemaHalls() {
        return cinemaHallService.getAllCinemaHalls();
    }

    @ModelAttribute("movies")
    public List<Movie> getMovies() {
        return movieService.getAllMovies();
    }

    @GetMapping()
    public String getAdminProgrammePage(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
            @RequestParam(required = false) String hallName,
            @ModelAttribute("cinemaHalls") List<CinemaHall> cinemaHalls,
            Model model
    ) {
        if(hallName != null && hallName.equals("all")) {
            hallName = null;
        }

        List<Programme> programmes = programmeService.getProgrammes(title, date, hallName);

        model.addAttribute("programmes", programmes);
        model.addAttribute("selectedHallName", hallName);
        model.addAttribute("selectedDate", date);
        model.addAttribute("selectedTitle", title);

        return "adminview/admin-programme-page";
    }

    @GetMapping("/edit/{id}")
    public String getEditProgrammeForm(
            @PathVariable Long id,
            @ModelAttribute("cinemaHalls") List<CinemaHall> cinemaHalls,
            @ModelAttribute("movies") List<Movie> movies,
            Model model
    ) {
        ProgrammeDto programmeDto = programmeService.getProgrammeDtoById(id);

        model.addAttribute("programme", programmeDto);
        model.addAttribute("movies", movies);
        return "adminview/programme-form";
    }

    @GetMapping("/edit")
    public String getAddProgrammeForm(
            @ModelAttribute("cinemaHalls") List<CinemaHall> cinemaHalls,
            @ModelAttribute("movies") List<Movie> movies,
            Model model
    ) {
        ProgrammeDto programmeDto = new ProgrammeDto();

        model.addAttribute("programme", programmeDto);
        return "adminview/programme-form";
    }

    @PostMapping("/edit")
    public String editProgramme(
            @Valid @ModelAttribute("programme") ProgrammeDto programmeDto,
            BindingResult theBindingResult,
            @ModelAttribute("cinemaHalls") List<CinemaHall> cinemaHalls,
            @ModelAttribute("movies") List<Movie> movies,
            Model model) {

        if(theBindingResult.hasErrors()) {
            model.addAttribute("programme", programmeDto);
            return "adminview/programme-form";
        }

        try {
            programmeService.update(programmeDto);
            return "redirect:/admin/programme";

        } catch (ValidationException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("programme", programmeDto);
            return "adminview/programme-form";
        }
    }

    @GetMapping("/delete")
    public String deleteProgramme(@RequestParam("idprogramme") Long id, Model model) {

        try {
            programmeService.deleteById(id);
            return "redirect:/admin/programme";

        } catch (ValidationException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("programme", id); // po co??
            return "redirect:/admin/programme"; // zwraca to samo
        }
    }
}
