package com.example.Cinema.service.Validators;

import com.example.Cinema.model.Dto.MovieDto;
import com.example.Cinema.repository.MovieRepository;
import org.springframework.stereotype.Service;

@Service
public class MovieValidationService {

    private final MovieRepository movieRepository;

    public MovieValidationService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public boolean isTitleValid(MovieDto movieDto) {
        return !movieRepository.existsByTitle(movieDto.getTitle(), movieDto.getIdmovie());
    }

    public boolean isImageValid(MovieDto movieDto) {
        return movieDto.getIdmovie() != null ||
                (movieDto.getImage() != null && !movieDto.getImage().isEmpty());
    }
}
