package com.example.Cinema.controller;

import com.example.Cinema.model.CinemaHall;
import com.example.Cinema.model.Dto.MovieDto;
import com.example.Cinema.model.Dto.ProgrammeDto;
import com.example.Cinema.model.Movie;
import com.example.Cinema.model.Programme;
import com.example.Cinema.service.CinemaHallService;
import com.example.Cinema.service.MovieService;
import com.example.Cinema.service.ProgrammeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@Controller
@RequestMapping("/admin/programme")
public class AdminProgrammeController {

    private final ProgrammeService programmeService;
    private final MovieService movieService;
    private final CinemaHallService cinemaHallService;

    @Autowired
    public AdminProgrammeController(ProgrammeService programmeService, MovieService movieService, CinemaHallService cinemaHallService) {
        this.programmeService = programmeService;
        this.movieService = movieService;
        this.cinemaHallService = cinemaHallService;
    }

    @GetMapping()
    public String getAdminProgrammePage(Model model) {
        List<Programme> programmes = programmeService.getAllProgrammes();

        List<Movie> movies = movieService.getAllMovies();
        List<MovieDto> movieDtoList = movies.stream().map(movie -> {
          String base64Imge = Base64.getEncoder().encodeToString(movie.getImageData());
          return new MovieDto(movie.getIdmovie(), movie.getTitle(), movie.getDescription(), movie.getDuration(), base64Imge);
        }).toList();

        List<CinemaHall> cinemaHalls = cinemaHallService.getAllCinemaHalls();

        model.addAttribute("cinemaHalls", cinemaHalls);
        model.addAttribute("movies", movieDtoList);
        model.addAttribute("programmes", programmes);

        return "adminview/admin-programme-page";
    }

    @GetMapping("/edit/{id}")
    public String getEditProgrammeForm(@PathVariable Long id, Model model) {
        ProgrammeDto programmeDto = new ProgrammeDto();

        Optional<Programme> programmeToUpdate = programmeService.getProgrammeById(id);

        if(programmeToUpdate.isPresent()) {
            programmeDto.setId(programmeToUpdate.get().getIdprogramme());
            programmeDto.setIdmovie(programmeToUpdate.get().getMovie().getIdmovie());
            programmeDto.setCinemaHallName(programmeToUpdate.get().getCinemaHall().getName());
            programmeDto.setDate(programmeToUpdate.get().getDate());
            programmeDto.setTime(programmeToUpdate.get().getTime());
        }

        model.addAttribute("cinemaHalls", cinemaHallService.getAllCinemaHalls());
        model.addAttribute("movies", movieService.getAllMovies());
        model.addAttribute("programme", programmeDto);

        return "adminview/programme-form";
    }

    @GetMapping("/edit")
    public String getAddProgrammeForm(Model model) {
        ProgrammeDto programmeDto = new ProgrammeDto();


        model.addAttribute("cinemaHalls", cinemaHallService.getAllCinemaHalls());
        model.addAttribute("movies", movieService.getAllMovies());
        model.addAttribute("programme", programmeDto);
        return "adminview/programme-form";
    }

    @PostMapping("/edit")
    public String editProgramme(@Valid @ModelAttribute("programme") ProgrammeDto programmeDto, BindingResult theBindingResult, Model model) {

        if(theBindingResult.hasErrors()) {

            model.addAttribute("cinemaHalls", cinemaHallService.getAllCinemaHalls());
            model.addAttribute("movies", movieService.getAllMovies());
            model.addAttribute("programme", programmeDto);
            return "adminview/programme-form";
        }

        Movie movie = movieService.getMovieById(programmeDto.getIdmovie());
        CinemaHall cinemaHall = cinemaHallService.getCinemaHallByName(programmeDto.getCinemaHallName());

        if(!programmeService.isCinemaHallAvailable(programmeDto, movie)) {

            model.addAttribute("hallAvailabilityError", "W tym czasie sala: " + programmeDto.getCinemaHallName() + " jest zajęta");
            model.addAttribute("cinemaHalls", cinemaHallService.getAllCinemaHalls());
            model.addAttribute("movies", movieService.getAllMovies());
            model.addAttribute("programme", programmeDto);

            return "adminview/programme-form";
        }

        Programme programme = programmeDto.getId() != null ? programmeService.getProgrammeById(programmeDto.getId()).orElse( new Programme()) : new Programme();

        programme.setMovie(movie);
        programme.setCinemaHall(cinemaHall);
        programme.setDate(programmeDto.getDate());
        programme.setTime(programmeDto.getTime());

        programmeService.save(programme);

        return "redirect:/admin/programme";
    }

    @GetMapping("/delete")
    public String deleteProgramme(@RequestParam("idprogramme") Long id){
        System.out.println("elo usuniete");
        programmeService.deleteById(id);

        return "redirect:/admin/programme";
    }
}
