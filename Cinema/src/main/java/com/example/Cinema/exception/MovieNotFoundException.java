package com.example.Cinema.exception;

public class MovieNotFoundException extends RuntimeException {

    public MovieNotFoundException(Long id) {
        super("Nie znaleziono filmu " + id);
    }
}
