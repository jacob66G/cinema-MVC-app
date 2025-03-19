package com.example.Cinema.service.Validators;

import com.example.Cinema.exception.MovieNotFoundException;
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

    public boolean isCinemaHallAvailable(ProgrammeDto programmeDto) {
        List<Programme> programmes = programmeRepository.findConflictingProgrammes(
                programmeDto.getCinemaHallName(),
                programmeDto.getDate(),
                programmeDto.getId()
        );
        Movie movie = movieRepository.findById(programmeDto.getIdmovie()).orElseThrow(() -> new MovieNotFoundException(programmeDto.getIdmovie()));

        LocalTime updatedProgrammeStartTime = programmeDto.getTime();
        LocalTime updatedProgrammeEndTime = programmeDto.getTime().plusMinutes(movie.getDuration() + 20);


        for(Programme programme : programmes) {
            LocalTime existProgrammeStartTime = programme.getTime();
            LocalTime existProgrammeEndTime = programme.getTime().plusMinutes(programme.getMovie().getDuration() + 20);

            if(!(updatedProgrammeStartTime.isAfter(existProgrammeEndTime) ||
                    (updatedProgrammeStartTime.isBefore(existProgrammeStartTime) && updatedProgrammeEndTime.isBefore(existProgrammeEndTime))
            )) {
                return false;
            }
        }
        return true;
    }

    public boolean isProgrammeCanBeEdit(Long id) {
        if(id == null) {
            return true;
        }

        return ticketRepository.findAllByProgramme_Idprogramme(id).isEmpty();
    }
}
