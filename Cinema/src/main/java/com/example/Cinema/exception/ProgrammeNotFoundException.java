package com.example.Cinema.exception;

public class ProgrammeNotFoundException extends RuntimeException {

    public ProgrammeNotFoundException(Long id) {
        super("Nie znalezino programu: " + id);
    }
}
