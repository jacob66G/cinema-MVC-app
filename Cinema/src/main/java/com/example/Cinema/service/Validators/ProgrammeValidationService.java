package com.example.Cinema.service.Validators;

import com.example.Cinema.exception.MovieNotFoundException;
import com.example.Cinema.exception.ValidationException;
import com.example.Cinema.model.dto.ProgrammeDto;
import com.example.Cinema.model.Movie;
import com.example.Cinema.model.Programme;
import com.example.Cinema.repository.MovieRepository;
import com.example.Cinema.repository.ProgrammeRepository;
import com.example.Cinema.repository.TicketRepository;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;

@Service
public class ProgrammeValidationService {

    private final ProgrammeRepository programmeRepository;
    private final MovieRepository movieRepository;
    private final TicketRepository ticketRepository;

    public ProgrammeValidationService(ProgrammeRepository programmeRepository, MovieRepository movieRepository,TicketRepository ticketRepository) {
        this.programmeRepository = programmeRepository;
        this.movieRepository = movieRepository;
        this.ticketRepository = ticketRepository;
    }

    public void validateCinemaHallAvailable(ProgrammeDto programmeDto) {
        List<Programme> programmes = programmeRepository.findConflictingProgrammes(
                programmeDto.getCinemaHallName(),
                programmeDto.getDate(),
                programmeDto.getId()
        );

        Movie movie = movieRepository.findById(programmeDto.getIdmovie()).orElseThrow(() -> new MovieNotFoundException(programmeDto.getIdmovie()));

        LocalTime newProgrammeStartTime = programmeDto.getTime();
        LocalTime newProgrammeEndTime = programmeDto.getTime().plusMinutes(movie.getDuration() + 20);

        for(Programme programme : programmes) {
            LocalTime existProgrammeStartTime = programme.getTime();
            LocalTime existProgrammeEndTime = programme.getTime().plusMinutes(programme.getMovie().getDuration() + 20);

            if(!(newProgrammeStartTime.isAfter(existProgrammeEndTime) ||
                    (newProgrammeStartTime.isBefore(existProgrammeStartTime) && newProgrammeEndTime.isBefore(existProgrammeEndTime))
            )) {
                throw new ValidationException("Sala kina jest zajeta w tym czasie: " + newProgrammeStartTime + " - " + newProgrammeEndTime);
            }
        }

    }

    public void validateEditPermission(Long id) {
        if(!ticketRepository.findAllByProgramme_Id(id).isEmpty()) {
            throw new ValidationException("Nie można zmodyfikować programu: "  +  id + " ponieważ są do niej przypisane inne obiekty.");
        }
    }
}
