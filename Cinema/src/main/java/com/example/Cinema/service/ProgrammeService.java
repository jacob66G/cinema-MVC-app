package com.example.Cinema.service;

import com.example.Cinema.model.Programme;
import com.example.Cinema.repository.ProgrammeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
