package com.example.Cinema.controller;

import com.example.Cinema.exception.ValidationException;
import com.example.Cinema.model.dto.MovieDto;
import com.example.Cinema.service.MovieService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@RequestMapping("/admin/movie")
public class AdminMovieController {

    private final MovieService movieService;

    @Autowired
    public AdminMovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping()
    public String getAdminMoviesPage(@RequestParam(required = false) String title, Model model) {
        List<MovieDto> moviesDto = (title == null || title.isBlank())
                ? movieService.getAllMoviesDto()
                : movieService.getMoviesDtoByTitle(title);

        if(title != null && !title.isBlank() && moviesDto.isEmpty()) {
            model.addAttribute("editMovieError", "Brak filmów o tytule: " + title);
            moviesDto = movieService.getAllMoviesDto();
        }

       model.addAttribute("movies", moviesDto);
       return "adminview/admin-movies-page";
    }


    @GetMapping("/edit/{id}")
    public String getEditMovieForm(@PathVariable Long id, Model model){
        MovieDto movieDto = movieService.getMovieDtoById(id);

        model.addAttribute("operation", "EDYTUJ");
        model.addAttribute("movie", movieDto);

        return "adminview/movie-form";
    }


    @GetMapping("/edit")
    public String getAddMovieForm(Model model){
        MovieDto movieDto = new MovieDto();

        model.addAttribute("operation", "DODAJ");
        model.addAttribute("movie", movieDto);

        return "adminview/movie-form";
    }


    @PostMapping("/edit")
    public String editMovie(@Valid @ModelAttribute("movie") MovieDto movieDto, BindingResult theBindingResult, Model model) {
        String operation = movieDto.getIdmovie() != null ? "EDYCJA" : "DODAJ";

        if(theBindingResult.hasErrors()) {
            model.addAttribute("operation", operation);
            model.addAttribute("movie", movieDto);
            return "adminview/movie-form";
        }

        try {
            movieService.updateMovie(movieDto);
            return "redirect:/admin/movie";

        } catch (ValidationException e) {
            model.addAttribute("operation", operation);
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("movie", movieDto);

            return "adminview/movie-form";

        } catch (RuntimeException e) {
            model.addAttribute("operation", operation);
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("movie", movieDto);

            return "adminview/movie-form";
        }
    }


    @PostMapping("/delete")
    public String deleteMovie(@RequestParam("movieId") Long id, Model model) {
        try {
            movieService.deleteById(id);
            return "redirect:/admin/movie";

        } catch (ValidationException e) {
            model.addAttribute("editMovieError", e.getMessage());
            model.addAttribute("movies", movieService.getAllMoviesDto());
            return "adminview/admin-movies-page";
        }
    }
}
