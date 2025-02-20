package com.example.Cinema.controller;

import com.example.Cinema.model.Movie;
import com.example.Cinema.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/admin/movie")
public class MovieController {

    private final MovieService movieService;

    @Autowired
    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }


    @GetMapping("/edit/{id}")
    public String editMoviePage(@PathVariable Long id, Model model){
        Optional<Movie> movie = movieService.findById(id);
        model.addAttribute("movie", movie.get());

        return "adminview/edit-movie-page";
    }

    @PostMapping("/edit/{id}")
    public String saveMovieEdits(
            @PathVariable Long id,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String imageAddress,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) Integer duration){

        Optional<Movie> movie = movieService.findById(id);

        if(movie.isPresent()){
            Movie updatedMovie = movie.get();
            updatedMovie.setTitle(title);
            updatedMovie.setImageAddress(imageAddress);
            updatedMovie.setDescription(description);
            updatedMovie.setDuration(duration);

            movieService.saveMovie(updatedMovie);
        }

        return "redirect:/admin/mainpage";
    }

    @GetMapping("/add")
    public String addMoviePage(){
        return "adminview/add-new-movie-page";
    }

    @PostMapping("/add")
    public String addMovie(
            @RequestParam String title,
            @RequestParam String imageAddress,
            @RequestParam String description,
            @RequestParam Integer duration,
            Model model) {

        if(movieService.existsByTitle(title)){
            model.addAttribute("errorMessage", "Film o tym tytule został już dodany!");
            return "adminview/add-new-movie-page";
        }

        Movie newMovie = new Movie(title, description, imageAddress, duration);
        movieService.saveMovie(newMovie);
        return "redirect:/admin/mainpage";
    }

    @PostMapping("delete/{id}")
    public String deleteMovie(@PathVariable Long id){
        movieService.deleteById(id);
        return "redirect:/admin/mainpage";
    }
}
