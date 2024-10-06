package com.example.Cinema.service;

import com.example.Cinema.model.CinemaHall;
import com.example.Cinema.model.Movie;
import com.example.Cinema.model.Programme;
import com.example.Cinema.repository.ProgrammeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProgrammeService {

    private final ProgrammeRepository programmeRepository;
    private final CinemaHallService cinemaHallService;

    @Autowired
    public ProgrammeService(ProgrammeRepository programmeRepository, CinemaHallService cinemaHallService) {
        this.programmeRepository = programmeRepository;
        this.cinemaHallService = cinemaHallService;
    }

    public void save(Programme programme) {
        programmeRepository.save(programme);
    }

    public void delete(Programme programme) {
        programmeRepository.delete(programme);
    }

    public Optional<Programme> findById(Long idprogrammme) {
        return programmeRepository.findById(idprogrammme);
    }

    public List<Programme> findByDate(LocalDate selectedDate) {
        return programmeRepository.findByDate(selectedDate);
    }


    public Optional<Programme> findByIdMovieAndDataAndTimeAndCinemaHall(Long id, LocalDate date, LocalTime time, String hall) {
        return programmeRepository.findByIdMovieAndDataAndTimeAndCinemaHall(id, date, time, hall);
    }

    public boolean checkHallAvailability(LocalDate date, LocalTime newProgrammeStart, LocalTime newProgrammeEnd, String hall) {
        List<Programme> programmeList = programmeRepository.findByDateAndHall(date, hall);

        for(Programme savedProgramme : programmeList){
            Movie savedMovie = savedProgramme.getMovie();
            LocalTime savedProgrammeEnd = savedProgramme.getTime().plusMinutes(savedMovie.getDuration() + 20);
            //TODO add entity hallReservation containing hallId, reservation data and time, and progrmme id
            if(newProgrammeStart.equals(savedProgramme.getTime()) ||
                (newProgrammeStart.isAfter(savedProgramme.getTime()) && newProgrammeStart.isBefore(savedProgrammeEnd)) ||
                (newProgrammeStart.isBefore(savedProgramme.getTime()) && newProgrammeEnd.isAfter(savedProgramme.getTime()))){
                return false;
            }
        }

        return true;
    }

    public Map<Movie, List<Programme>> getProgrammeListGroupedByMovie(List<Programme> programmeDate) {

        Map<Movie, List<Programme>> programmeListGroupedByMovie = programmeDate.stream()
                .collect(Collectors.groupingBy(
                        Programme::getMovie,
                        () -> new TreeMap<>(Comparator.comparing(Movie::getTitle)),
                        Collectors.toList()
                ));

        return programmeListGroupedByMovie;
    }

    public Map<Movie, Set<String>> getHallsForMovie(Map<Movie, List<Programme>> programmeListGroupedByMovie) {
        Map<Movie, Set<String>> hallsForMovie = new HashMap<>();

        for(Map.Entry<Movie, List<Programme>> entry : programmeListGroupedByMovie.entrySet()){
            Movie movie1 = entry.getKey();
            List<Programme> programmes = entry.getValue();

            Set<String> hallNames = programmes.stream()
                    .map(p -> p.getCinemaHall().getName())
                    .collect(Collectors.toSet());

            hallsForMovie.put(movie1, hallNames);
        }

        return hallsForMovie;
    }

    public String handleAddProgramme(Movie movie, LocalDate date, LocalTime time, String hall) {
        CinemaHall cinemaHall = cinemaHallService.findByName(hall);
        if (cinemaHall == null){ return "Niepoprawna sala. Dostępne sale: A, B, C";}

        LocalTime newProgrammeStart = time;
        LocalTime newProgrammeEnd = time.plusMinutes(movie.getDuration() + 20);

        boolean hallAvailability = checkHallAvailability(date, newProgrammeStart, newProgrammeEnd, hall);
        if(hallAvailability){
            Programme newProgramme = new Programme(movie, date, time,  cinemaHall);
            save(newProgramme);
            return null;
        } else {
            return "Sala " + hall + " " + date + " o " + time + " jest zajęta";
        }
    }

    public String handleDeleteProgramme(Long movieId, LocalDate date, LocalTime time, String hall) {
        Optional<Programme> savedProgramme = findByIdMovieAndDataAndTimeAndCinemaHall(movieId, date, time, hall);
        if (!savedProgramme.isPresent()) {
            return "Film nie jest grany " + date + " " + time + " w sali " + hall + ". Wprowadź poprawne dane.";
        }
        delete(savedProgramme.get());
        return null;
    }
}
