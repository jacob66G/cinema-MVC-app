package com.example.Cinema.service;

import com.example.Cinema.model.Dto.MovieDto;
import com.example.Cinema.model.Movie;
import com.example.Cinema.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class MovieService {
    private final MovieRepository movieRepository;

    @Autowired
    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public void save(Movie movie) {
        movieRepository.save(movie);
    }

    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    public Optional<Movie> findById(Long id) {
        return movieRepository.findById(id);
    }

    public void deleteById(Long id) {
        movieRepository.deleteById(id);
    }

    public void updateMovie(MovieDto movieDto) {
        Movie movie;

        if(movieDto.getIdmovie() != null) {
            movie = movieRepository.findById(movieDto.getIdmovie()).orElseThrow(() -> new RuntimeException("Movie with id: " + movieDto.getIdmovie() +" not found"));
        }
        else {
            movie = new Movie();
        }

        movie.setTitle(movieDto.getTitle());
        movie.setDescription(movieDto.getDescription());
        movie.setDuration(movieDto.getDuration());

        if(movieDto.getImage() != null && !movieDto.getImage().isEmpty()) {
            try {
                byte[] imageData = movieDto.getImage().getBytes();
                movie.setImageData(imageData);
            }
            catch (IOException e) {
                throw new RuntimeException("Błąd wczytywania obrazu", e);
            }
        }

        movieRepository.save(movie);
    }
}
