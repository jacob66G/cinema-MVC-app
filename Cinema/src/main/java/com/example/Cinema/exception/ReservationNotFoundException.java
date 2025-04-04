package com.example.Cinema.exception;

public class ReservationNotFoundException extends RuntimeException {
    public ReservationNotFoundException(Long id) {
        super("Nie znaleziono reservacji: " + id);
    }
}
