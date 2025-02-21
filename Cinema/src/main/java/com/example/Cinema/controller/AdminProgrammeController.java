package com.example.Cinema.controller;

import com.example.Cinema.model.CinemaHall;
import com.example.Cinema.model.Dto.MovieDto;
import com.example.Cinema.model.Dto.ProgrammeDto;
import com.example.Cinema.model.Movie;
import com.example.Cinema.model.Programme;
import com.example.Cinema.service.CinemaHallService;
import com.example.Cinema.service.MovieService;
import com.example.Cinema.service.ProgrammeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

//        List<ProgrammeDto> programmeDtoList = programmes.stream()
//            .map(programme -> new ProgrammeDto(
//                    programme.getIdprogramme(),
//                    programme.getDate(),
//                    programme.getTime(),
//                    programme.getMovie().getTitle(),
//                    programme.getCinemaHall().getName()
//            ))
//            .toList();


        List<Movie> movies = movieService.getAllMovies();
        List<MovieDto> movieDtoList = movies.stream().map(movie -> {
          String base64Imge = Base64.getEncoder().encodeToString(movie.getImageData());
          return new MovieDto(movie.getIdmovie(), movie.getTitle(), movie.getDescription(), movie.getDuration(), base64Imge);
        }).toList();

        List<CinemaHall> cinemaHalls = cinemaHallService.getAllCinemaHalls();

        model.addAttribute("cinemaHalls", cinemaHalls);
        model.addAttribute("movies", movieDtoList);
        model.addAttribute("programmes", programmes);
        return "adminview/edit-movie-programme";
    }

    @PostMapping("/add")
    public String addProgramme(Model model) {

        return "redirect:/admin/programme";
    }

    @PostMapping("/edit")
    public String editProgramme(Model model){

        return "redirect:/admin/programme";
    }

    @PostMapping("/delete")
    public String deleteProgramme(@RequestParam("programmeId") Long id){
        programmeService.deleteById(id);

        return "redirect:/admin/programme";
    }
}
