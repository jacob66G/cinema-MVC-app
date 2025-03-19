package com.example.Cinema.service.Validators;

import org.springframework.stereotype.Service;

@Service
public class ClientValidationService {

    public boolean isPasswordValid(String password, String confirmPassword) {
        return password.equals(confirmPassword);
    }
}
