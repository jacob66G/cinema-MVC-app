package com.example.Cinema.service;

import com.example.Cinema.model.Reservation;
import com.example.Cinema.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;

    @Autowired
    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public void save(Reservation reservation) {
        reservationRepository.save(reservation);
    }

    public String checkEmailAndPhoneNumber(String phoneNumber, String email, String confirm_email) {

        if(!email.equals(confirm_email)){
            return "Adresy email różnią się";
        }
        else if (!phoneNumber.chars().allMatch(Character::isDigit)) {
            return "Wprowadź poprawny numer";
        }
        else {
            return null;
        }
    }
}
