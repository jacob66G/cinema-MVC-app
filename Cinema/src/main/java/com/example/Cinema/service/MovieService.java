package com.example.Cinema.service;

import com.example.Cinema.model.Movie;
import com.example.Cinema.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        List<Movie> movies = movieRepository.findAll();
        return movies;
    }

    public void deleteById(Long id) {
        movieRepository.deleteById(id);
    }

    public Optional<Movie> findById(Long id) {
        return movieRepository.findById(id);
    }

    public Boolean existsByTitle(String title) {
        return movieRepository.existsByTitle(title);
    }
}
