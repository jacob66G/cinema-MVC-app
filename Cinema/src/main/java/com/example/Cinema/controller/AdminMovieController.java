package com.example.Cinema.controller;

import com.example.Cinema.model.Dto.MovieDto;
import com.example.Cinema.model.Movie;
import com.example.Cinema.service.MovieService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;


@Controller
@RequestMapping("/admin/movie")
public class AdminMovieController {

    private final MovieService movieService;

    @Autowired
    public AdminMovieController(MovieService movieService) {
        this.movieService = movieService;
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

        Optional<Movie> movieToUpdate = movieService.findById(id);

        if(movieToUpdate.isPresent()){
            movieDto.setIdmovie(movieToUpdate.get().getIdmovie());
            movieDto.setTitle(movieToUpdate.get().getTitle());
            movieDto.setDescription(movieToUpdate.get().getDescription());
            movieDto.setDuration(movieToUpdate.get().getDuration());
            movieDto.setBase64Image(Base64.getEncoder().encodeToString(movieToUpdate.get().getImageData()));
        }

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
    public String editMovie(@Valid @ModelAttribute("movie") MovieDto movieDto, BindingResult theBindingResult, Model model) throws IOException {
        String operation;
        Movie movie;

        if(movieDto.getIdmovie() != null){
            movie = movieService.findById(movieDto.getIdmovie()).get();
            operation = "EDYTUJ";
        } else {
            movie = new Movie();
            operation = "DODAJ";
        }

        if(theBindingResult.hasErrors()) {
            model.addAttribute("operation", operation);
            model.addAttribute("movie", movieDto);

            return "adminview/movie-form";
        }

        if(movieDto.getIdmovie() == null && (movieDto.getImage() == null || movieDto.getImage().isEmpty())) {
            model.addAttribute("operation", operation);
            model.addAttribute("imageError", "Zdjecie jest wymagane");
            model.addAttribute("movie", movieDto);

            return "adminview/movie-form";
        }

        if(!movieDto.getTitle().equalsIgnoreCase(movie.getTitle()) && movieService.existsByTitle(movieDto.getTitle())) {
            model.addAttribute("operation", operation);
            model.addAttribute("titleError", "Film o tym tytule jest już dodany");
            model.addAttribute("movie", movieDto);

            return "adminview/movie-form";
        }

        movie.setTitle(movieDto.getTitle());
        movie.setDescription(movieDto.getDescription());
        movie.setDuration(movieDto.getDuration());

        if(movieDto.getImage() != null && !movieDto.getImage().isEmpty()) {
            byte[] imageData = movieDto.getImage().getBytes();
            movie.setImageData(imageData);
        }

        movieService.save(movie);

        return "redirect:/admin/movie";
    }

    @PostMapping("/delete")
    public String deleteMovie(@RequestParam("movieId") Long id){
        movieService.deleteById(id);

        return "redirect:/admin/movie";
    }
}
