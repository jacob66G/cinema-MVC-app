package com.example.Cinema.exception;

public class SeatNotFoundException extends RuntimeException {
    public SeatNotFoundException(Long id) {
        super("Nie znaleziono seat z id: " + id);
    }
}
