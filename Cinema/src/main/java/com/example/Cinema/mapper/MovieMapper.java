package com.example.Cinema.mapper;

import com.example.Cinema.model.dto.MovieDto;
import com.example.Cinema.model.Movie;
import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
public class MovieMapper {

    public MovieDto toDto(Movie movie) {
        String base64Image = Base64.getEncoder().encodeToString(movie.getImageData());

        return new MovieDto(
                movie.getId(),
                movie.getTitle(),
                movie.getDescription(),
                movie.getDuration(),
                base64Image
        );
    }
}
