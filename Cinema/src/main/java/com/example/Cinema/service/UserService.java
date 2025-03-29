package com.example.Cinema.service;

import com.example.Cinema.model.User;
import com.example.Cinema.model.dto.ClientDto;
import com.example.Cinema.model.dto.EmailChangeDto;
import com.example.Cinema.model.dto.PasswordChangeDto;
import com.example.Cinema.model.dto.PersonalDataDto;
import com.example.Cinema.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    
    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void changeClientPersonalData(User user, PersonalDataDto personalDataDto) {
        user.setName(personalDataDto.getName());
        user.setSurname(personalDataDto.getSurname());
        user.setPhone(personalDataDto.getPhone());
        userRepository.save(user);
    }

    public void changeClientPassword(User user, PasswordChangeDto passwordChangeDto) {
        user.setPassword(passwordEncoder.encode(passwordChangeDto.getNewPassword()));
        userRepository.save(user);
    }

    public void changeClientEmail(User user, EmailChangeDto emailChangeDto) {
        user.setUserName(emailChangeDto.getEmail());
        userRepository.save(user);
    }

    public User findByUserName(String username) {
        return userRepository.findByUserName(username);
    }
}
