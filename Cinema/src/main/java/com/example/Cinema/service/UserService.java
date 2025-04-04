package com.example.Cinema.service;

import com.example.Cinema.model.User;
import com.example.Cinema.model.dto.EmailChangeDto;
import com.example.Cinema.model.dto.PasswordChangeDto;
import com.example.Cinema.model.dto.PersonalDataDto;
import com.example.Cinema.repository.UserRepository;
import com.example.Cinema.service.Validators.UserValidationService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    
    private final UserRepository userRepository;
    private final UserValidationService userValidationService;
    private final BCryptPasswordEncoder passwordEncoder;
    
    public UserService(UserRepository userRepository, UserValidationService userValidationService, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userValidationService = userValidationService;
        this.passwordEncoder = passwordEncoder;
    }

    public void changeClientPersonalData(User user, PersonalDataDto personalDataDto) {
        userValidationService.validateExistsByPhone(user.getPhone());

        user.setName(personalDataDto.getName());
        user.setSurname(personalDataDto.getSurname());
        user.setPhone(personalDataDto.getPhone());
        userRepository.save(user);
    }

    public void changeClientPassword(User user, PasswordChangeDto passwordChangeDto) {
        userValidationService.validateOldPassword(user, passwordChangeDto.getCurrentPassword());
        userValidationService.validatePasswordMatch(passwordChangeDto.getNewPassword(), passwordChangeDto.getConfirmPassword());

        user.setPassword(passwordEncoder.encode(passwordChangeDto.getNewPassword()));
        userRepository.save(user);

    }

    public void changeClientEmail(User user, EmailChangeDto emailChangeDto) {
        userValidationService.validateEmailMatch(emailChangeDto.getEmail(), emailChangeDto.getConfirmEmail());
        userValidationService.validateExistsByEmail(emailChangeDto.getEmail());

        user.setUserName(emailChangeDto.getEmail());
        userRepository.save(user);
    }

    public User findByUserName(String username) {
        return userRepository.findByUserName(username);
    }
}
