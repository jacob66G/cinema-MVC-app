package com.example.Cinema.service.Validators;

import com.example.Cinema.model.Dto.ProgrammeDto;
import com.example.Cinema.model.Movie;
import com.example.Cinema.model.Programme;
import com.example.Cinema.repository.MovieRepository;
import com.example.Cinema.repository.ProgrammeRepository;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;

@Service
public class ProgrammeValidationService {

    private final ProgrammeRepository programmeRepository;
    private final MovieRepository movieRepository;

    public ProgrammeValidationService(ProgrammeRepository programmeRepository, MovieRepository movieRepository) {
        this.programmeRepository = programmeRepository;
        this.movieRepository = movieRepository;
    }

    public boolean isCinemaHallAvailable(ProgrammeDto programmeDto) {
        List<Programme> programmes = programmeRepository.findConflictingProgrammes(
                programmeDto.getCinemaHallName(),
                programmeDto.getDate(),
                programmeDto.getId()
        );
        Movie movie = movieRepository.findById(programmeDto.getIdmovie()).orElseThrow();

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
}
