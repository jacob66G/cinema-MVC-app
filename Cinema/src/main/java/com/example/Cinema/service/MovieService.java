package com.example.Cinema.service;

import com.example.Cinema.exception.MovieNotFoundException;
import com.example.Cinema.mapper.MovieMapper;
import com.example.Cinema.model.dto.MovieDto;
import com.example.Cinema.model.Movie;
import com.example.Cinema.repository.MovieRepository;
import com.example.Cinema.repository.TicketRepository;
import com.example.Cinema.service.Validators.MovieValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class MovieService {
    private final MovieRepository movieRepository;
    private final MovieValidationService movieValidationService;
    private final MovieMapper movieMapper;

    @Autowired
    public MovieService(MovieRepository movieRepository, MovieValidationService movieValidationService, MovieMapper movieMapper) {
        this.movieRepository = movieRepository;
        this.movieValidationService = movieValidationService;
        this.movieMapper = movieMapper;
    }

    public void save(Movie movie) {
        movieRepository.save(movie);
    }

    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    public List<MovieDto> getAllMoviesDto() {
        return movieRepository.findAll().stream().map(movieMapper::toDto).toList();
    }

    public List<MovieDto> getMoviesDtoByTitle(String title) {
        return movieRepository.findMovieByTitle(title).stream().map(movieMapper::toDto).toList();
    }

    public MovieDto getMovieDtoById(Long id) {
        Movie movie = movieRepository.findById(id).orElseThrow(() -> new MovieNotFoundException(id));

        return movieMapper.toDto(movie);
    }

    public void deleteById(Long id) {
        movieValidationService.validateEditPermission(id);
        movieRepository.deleteById(id);
    }

    public void updateMovie(MovieDto movieDto) {
        movieValidationService.validateImage(movieDto);
        movieValidationService.validateTitleUniqueness(movieDto.getTitle(), movieDto.getIdmovie());
        movieValidationService.validateEditPermission(movieDto.getIdmovie());

        Movie movie;

        if (movieDto.getIdmovie() != null) {
            movie = movieRepository.findById(movieDto.getIdmovie()).orElseThrow(() -> new MovieNotFoundException(movieDto.getIdmovie()));
        } else {
            movie = new Movie();
        }

        movie.setTitle(movieDto.getTitle());
        movie.setDescription(movieDto.getDescription());
        movie.setDuration(movieDto.getDuration());

        if (movieDto.getImage() != null && !movieDto.getImage().isEmpty()) {
            try {
                byte[] imageData = movieDto.getImage().getBytes();
                movie.setImageData(imageData);
            } catch (IOException e) {
                throw new RuntimeException("Błąd wczytywania obrazu", e);
            }
        }

        movieRepository.save(movie);
    }
}
