package com.example.Cinema.service.Validators;

import com.example.Cinema.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class ClientValidationService {

    private final UserRepository userRepository;

    public ClientValidationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean isPasswordValid(String password, String confirmPassword) {
        return password.equals(confirmPassword);
    }

    public boolean findByUserEmail(String email) {
        return userRepository.findByUserEmail(email);
    }

    public boolean findByUserPhone(String phone) {
        return userRepository.findByUserPhone(phone);
    }
}
