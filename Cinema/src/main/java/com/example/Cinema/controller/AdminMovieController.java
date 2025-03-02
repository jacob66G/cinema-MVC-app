package com.example.Cinema.controller;

import com.example.Cinema.model.Dto.MovieDto;
import com.example.Cinema.model.Movie;
import com.example.Cinema.service.MovieService;
import com.example.Cinema.service.Validators.MovieValidationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.util.Base64;
import java.util.List;


@Controller
@RequestMapping("/admin/movie")
public class AdminMovieController {

    private final MovieService movieService;
    private final MovieValidationService movieValidationService;

    @Autowired
    public AdminMovieController(MovieService movieService, MovieValidationService movieValidationService) {
        this.movieService = movieService;
        this.movieValidationService = movieValidationService;
    }

    @GetMapping()
    public String getAdminMoviesPage(Model model) {
       List<Movie> movies = movieService.getAllMovies();

       List<MovieDto> movieDtos = movies.stream().map(movie -> {
           String base64Image = Base64.getEncoder().encodeToString(movie.getImageData());
           return new MovieDto(movie.getIdmovie(), movie.getTitle(), movie.getDescription(), movie.getDuration(), base64Image);
       }).toList();

       model.addAttribute("movies", movieDtos);
       return "adminview/admin-movies-page";
    }


    @GetMapping("/edit/{id}")
    public String getEditMovieForm(@PathVariable Long id, Model model){
        MovieDto movieDto = new MovieDto();

        Movie movieToUpdate = movieService.findById(id).orElseThrow();

        movieDto.setIdmovie(movieToUpdate.getIdmovie());
        movieDto.setTitle(movieToUpdate.getTitle());
        movieDto.setDescription(movieToUpdate.getDescription());
        movieDto.setDuration(movieToUpdate.getDuration());
        movieDto.setBase64Image(Base64.getEncoder().encodeToString(movieToUpdate.getImageData()));

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

        if(!movieValidationService.isImageValid(movieDto)) {
            model.addAttribute("operation", operation);
            model.addAttribute("imageError", "Zdjecie jest wymagane");
            model.addAttribute("movie", movieDto);

            return "adminview/movie-form";
        }

        if(!movieValidationService.isTitleValid(movieDto)) {
            model.addAttribute("operation", operation);
            model.addAttribute("titleError", "Film o tym tytule jest już dodany");
            model.addAttribute("movie", movieDto);

            return "adminview/movie-form";
        }

        movieService.updateMovie(movieDto);

        return "redirect:/admin/movie";
    }

    @PostMapping("/delete")
    public String deleteMovie(@RequestParam("movieId") Long id, Model model) {
        if(!movieValidationService.isMovieCanBeEdit(id)) {
            model.addAttribute("editMovieError", "Film jest używany w systemie rezerwacji. Nie można go usunąć");
            return "redirect:/admin/movie";
        }
        movieService.deleteById(id);

        return "redirect:/admin/movie";
    }
}
