package com.example.Cinema.service.Validators;

import com.example.Cinema.model.Dto.MovieDto;
import com.example.Cinema.repository.MovieRepository;
import com.example.Cinema.repository.TicketRepository;
import org.springframework.stereotype.Service;

@Service
public class MovieValidationService {

    private final MovieRepository movieRepository;
    private final TicketRepository ticketRepository;

    public MovieValidationService(MovieRepository movieRepository, TicketRepository ticketRepository) {
        this.movieRepository = movieRepository;
        this.ticketRepository = ticketRepository;
    }

    public boolean isTitleValid(MovieDto movieDto) {
        return !movieRepository.existsByTitle(movieDto.getTitle(), movieDto.getIdmovie());
    }

    public boolean isImageValid(MovieDto movieDto) {
        return movieDto.getIdmovie() != null ||
                (movieDto.getImage() != null && !movieDto.getImage().isEmpty());
    }

    public boolean isMovieCanBeEdit(Long id) {
        if(id == null) return true;

        return ticketRepository.findAllByProgramme_Movie_Idmovie(id).isEmpty();
    }
}
