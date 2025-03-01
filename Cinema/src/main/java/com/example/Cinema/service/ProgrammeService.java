package com.example.Cinema.service;

import com.example.Cinema.model.Dto.ProgrammeDto;
import com.example.Cinema.model.Movie;
import com.example.Cinema.model.Programme;
import com.example.Cinema.repository.ProgrammeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Service
public class ProgrammeService {

    private final ProgrammeRepository programmeRepository;

    @Autowired
    public ProgrammeService(ProgrammeRepository programmeRepository) {
        this.programmeRepository = programmeRepository;
    }

    public List<Programme> getAllProgrammes() {
        return programmeRepository.findAll();
    }

    public void save(Programme programme) {
        programmeRepository.save(programme);
    }

    public void deleteById(Long id) {
        programmeRepository.deleteById(id);
    }

    public Optional<Programme> getProgrammeById(Long id) {
        return programmeRepository.findById(id);
    }

    public boolean isCinemaHallAvailable(ProgrammeDto programmeDto, Movie movie) {

        List<Programme> programmes = programmeRepository.findConflictingProgrammes(
                programmeDto.getCinemaHallName(),
                programmeDto.getDate(),
                programmeDto.getId()
        );

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

    public List<Programme> getProgrammes(LocalDate date, String hallName) {
        if(date != null && hallName != null && !hallName.equals("all")) {
            return programmeRepository.findByDateAndCinemaHall_Name(date, hallName);
        }
        else if(date != null){
            return programmeRepository.findByDate(date);
        }
        else if(hallName != null) {
            return programmeRepository.findByCinemaHall_Name(hallName);
        }

        return programmeRepository.findAll();
    }
}
