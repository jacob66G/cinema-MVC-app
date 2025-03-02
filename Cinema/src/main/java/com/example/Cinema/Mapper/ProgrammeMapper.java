package com.example.Cinema.Mapper;

import com.example.Cinema.model.Dto.ProgrammeDto;
import com.example.Cinema.model.Movie;
import com.example.Cinema.model.Programme;
import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
public class ProgrammeMapper {

    public ProgrammeDto toDto(Programme programme) {
        Movie movie = programme.getMovie();
        String base64Image = Base64.getEncoder().encodeToString(movie.getImageData());

        return new ProgrammeDto(
                programme.getIdprogramme(),
                programme.getDate(),
                programme.getTime(),
                movie.getTitle(),
                base64Image
        );
    }
}
