package com.example.Cinema.service.Validators;

import com.example.Cinema.exception.ValidationException;
import com.example.Cinema.model.dto.MovieDto;
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

    public void validateTitleUniqueness(String title, Long currentMovieId) {
        if (movieRepository.existsByTitle(title, currentMovieId)) {
            throw new ValidationException("Film o tytule "+ title + " już istnieje");
        }
    }

    public void validateEditPermission(Long movieId) {
        boolean hasTickets = !ticketRepository.findAllByProgramme_Movie_Id(movieId).isEmpty();

        if (hasTickets) {
            throw new ValidationException("Nie można zmodyfikować filmu: "  +  movieId + " ponieważ są do niej przypisane inne obiekty.");
        }
    }

    public void validateImage(MovieDto movieDto) {
        boolean isNew = movieDto.getIdmovie() == null;
        boolean hasImage = movieDto.getImage() != null && !movieDto.getImage().isEmpty();

        if (isNew && !hasImage) {
            throw new ValidationException("Nowy film musi mieć przypisany obraz");
        }
    }
}
