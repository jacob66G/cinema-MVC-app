package com.example.Cinema.Mapper;

import com.example.Cinema.model.Dto.MovieDto;
import com.example.Cinema.model.Movie;
import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
public class MovieMapper {

    public MovieDto toDto(Movie movie) {
        String base64Image = Base64.getEncoder().encodeToString(movie.getImageData());

        return new MovieDto(
                movie.getIdmovie(),
                movie.getTitle(),
                movie.getDescription(),
                movie.getDuration(),
                base64Image
        );
    }
}
