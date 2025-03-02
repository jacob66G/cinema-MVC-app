package com.example.Cinema.controller;

import com.example.Cinema.model.CinemaHall;
import com.example.Cinema.model.Dto.ProgrammeDto;
import com.example.Cinema.model.Movie;
import com.example.Cinema.model.Programme;
import com.example.Cinema.service.CinemaHallService;
import com.example.Cinema.service.MovieService;
import com.example.Cinema.service.ProgrammeService;
import com.example.Cinema.service.Validators.ProgrammeValidationService;
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
    private final ProgrammeValidationService programmeValidationService;


    @Autowired
    public AdminProgrammeController(ProgrammeService programmeService, MovieService movieService, CinemaHallService cinemaHallService, ProgrammeValidationService programmeValidationService) {
        this.programmeService = programmeService;
        this.movieService = movieService;
        this.cinemaHallService = cinemaHallService;
        this.programmeValidationService = programmeValidationService;
    }

    @ModelAttribute("cinemaHalls")
    public List<CinemaHall> getCinemaHalls(Model model) {
        return cinemaHallService.getAllCinemaHalls();
    }

    @ModelAttribute("movies")
    public List<Movie> getMovies(Model model) {
        return movieService.getAllMovies();
    }

    @GetMapping()
    public String getAdminProgrammePage(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
            @RequestParam(required = false) String hallName,
            @ModelAttribute("cinemaHalls") List<CinemaHall> cinemaHalls,
            Model model
    ) {
        List<Programme> programmes = programmeService.getProgrammes(date, hallName);

        model.addAttribute("programmes", programmes);
        model.addAttribute("selectedHallName", hallName);
        model.addAttribute("selectedDate", date);

        return "adminview/admin-programme-page";
    }

    @GetMapping("/edit/{id}")
    public String getEditProgrammeForm(
            @PathVariable Long id,
            @ModelAttribute("cinemaHalls") List<CinemaHall> cinemaHalls,
            @ModelAttribute("movies") List<Movie> movies,
            Model model) {
        ProgrammeDto programmeDto = new ProgrammeDto();

        Programme programmeToUpdate = programmeService.getProgrammeById(id).orElseThrow();

        programmeDto.setId(programmeToUpdate.getIdprogramme());
        programmeDto.setIdmovie(programmeToUpdate.getMovie().getIdmovie());
        programmeDto.setCinemaHallName(programmeToUpdate.getCinemaHall().getName());
        programmeDto.setDate(programmeToUpdate.getDate());
        programmeDto.setTime(programmeToUpdate.getTime());

        model.addAttribute("programme", programmeDto);

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

        if(!programmeValidationService.isCinemaHallAvailable(programmeDto)) {
            model.addAttribute("hallAvailabilityError", "W tym czasie sala: " + programmeDto.getCinemaHallName() + " jest zajęta");
            model.addAttribute("programme", programmeDto);
            return "adminview/programme-form";
        }

        programmeService.updateProgramme(programmeDto);

        return "redirect:/admin/programme";
    }

    @GetMapping("/delete")
    public String deleteProgramme(@RequestParam("idprogramme") Long id){
        programmeService.deleteById(id);

        return "redirect:/admin/programme";
    }
}
