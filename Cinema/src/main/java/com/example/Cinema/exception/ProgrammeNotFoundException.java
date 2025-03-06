package com.example.Cinema.exception;

public class ProgrammeNotFoundException extends RuntimeException {

    public ProgrammeNotFoundException(Long id) {
        super("Could not find progrmme: " + id);
    }
}
