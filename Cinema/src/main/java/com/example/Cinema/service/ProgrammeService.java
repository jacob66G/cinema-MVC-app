package com.example.Cinema.service;

import com.example.Cinema.exception.MovieNotFoundException;
import com.example.Cinema.exception.ProgrammeNotFoundException;
import com.example.Cinema.mapper.ProgrammeMapper;
import com.example.Cinema.model.CinemaHall;
import com.example.Cinema.model.dto.ProgrammeDto;
import com.example.Cinema.model.Movie;
import com.example.Cinema.model.Programme;
import com.example.Cinema.repository.CinemaHallRepository;
import com.example.Cinema.repository.MovieRepository;
import com.example.Cinema.repository.ProgrammeRepository;
import com.example.Cinema.service.Validators.ProgrammeValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class ProgrammeService {

    private final ProgrammeRepository programmeRepository;
    private final MovieRepository movieRepository;
    private final CinemaHallRepository cinemaHallRepository;
    private final ProgrammeMapper programmeMapper;
    private final ProgrammeValidationService  programmeValidationService;

    @Autowired
    public ProgrammeService(ProgrammeRepository programmeRepository, MovieRepository movieRepository, CinemaHallRepository cinemaHallRepository, ProgrammeMapper programmeMapper, ProgrammeValidationService programmeValidationService) {
        this.programmeRepository = programmeRepository;
        this.movieRepository = movieRepository;
        this.cinemaHallRepository = cinemaHallRepository;
        this.programmeMapper = programmeMapper;
        this.programmeValidationService = programmeValidationService;
    }

    public void save(Programme programme) {
        programmeRepository.save(programme);
    }

    public void deleteById(Long id) {
        programmeValidationService.validateEditPermission(id);

        programmeRepository.deleteById(id);
    }

    public Programme getProgrammeById(Long id) {
        return programmeRepository.findById(id).orElseThrow(() -> new ProgrammeNotFoundException(id));
    }

    public ProgrammeDto getProgrammeDtoById(Long id) {
        Programme programme = programmeRepository.findById(id).orElseThrow(() -> new ProgrammeNotFoundException(id));
        return programmeMapper.toDto(programme);
    }

    public List<Programme> getProgrammes(LocalDate date) {
        return programmeRepository.findByDate(date);
    }

    public List<Programme> getProgrammes(String title, LocalDate date, String hallName) {
        return programmeRepository.findByTitleDateHallName(title, date, hallName);
    }

    public void update(ProgrammeDto programmeDto) {
        programmeValidationService.validateEditPermission(programmeDto.getId());
        programmeValidationService.validateCinemaHallAvailable(programmeDto);

        Programme programme = programmeRepository.findById(programmeDto.getId()).orElse(new Programme());

        Movie movie = movieRepository.findById(programmeDto.getIdmovie()).orElseThrow(() -> new MovieNotFoundException(programmeDto.getIdmovie()));
        CinemaHall cinemaHall = cinemaHallRepository.findCinemaHallByName(programmeDto.getCinemaHallName());

        programme.setMovie(movie);
        programme.setCinemaHall(cinemaHall);
        programme.setDate(programmeDto.getDate());
        programme.setTime(programmeDto.getTime());

        save(programme);
    }

}
