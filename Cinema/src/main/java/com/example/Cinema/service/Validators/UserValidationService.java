package com.example.Cinema.service.Validators;

import com.example.Cinema.model.User;
import com.example.Cinema.repository.UserRepository;
import jakarta.validation.constraints.NotBlank;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserValidationService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserValidationService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public boolean isPasswordValid(String password, String confirmPassword) {
        return password.equals(confirmPassword);
    }

    public boolean isEmailValid(String email, String confirmedEmail) {
        return email.equals(confirmedEmail);
    }

    public boolean findByUserEmail(String email) {
        return userRepository.findByUserEmail(email);
    }

    public boolean findByUserPhone(String phone) {
        return userRepository.findByUserPhone(phone);
    }

    public boolean isOldPasswordValid(User user, String currentPassword) {
        return bCryptPasswordEncoder.matches(currentPassword, user.getPassword());
    }
}
