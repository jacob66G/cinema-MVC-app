package com.example.Cinema.service;

import com.example.Cinema.mapper.ClientMapper;
import com.example.Cinema.model.User;
import com.example.Cinema.model.dto.ClientDto;
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
    private final ClientMapper clientMapper;
    
    public UserService(UserRepository userRepository, UserValidationService userValidationService, BCryptPasswordEncoder passwordEncoder, ClientMapper clientMapper) {
        this.userRepository = userRepository;
        this.userValidationService = userValidationService;
        this.passwordEncoder = passwordEncoder;
        this.clientMapper = clientMapper;
    }

    public void changePersonalData(User user, PersonalDataDto personalDataDto) {
        userValidationService.validateExistsByPhone(personalDataDto.getPhone(), user.getId());

        user.setName(personalDataDto.getName());
        user.setSurname(personalDataDto.getSurname());
        user.setPhone(personalDataDto.getPhone());
        userRepository.save(user);
    }

    public void changePassword(User user, PasswordChangeDto passwordChangeDto) {
        userValidationService.validateOldPassword(user, passwordChangeDto.getCurrentPassword());
        userValidationService.validatePasswordMatch(passwordChangeDto.getNewPassword(), passwordChangeDto.getConfirmPassword());

        user.setPassword(passwordEncoder.encode(passwordChangeDto.getNewPassword()));
        userRepository.save(user);

    }

    public User getByName(String username) {
        return userRepository.findByUserName(username);
    }

    public void save(ClientDto clientDto) {
        userValidationService.validateExistsByEmail(clientDto.getEmail());
        userValidationService.validateExistsByPhone(clientDto.getPhone());
        userValidationService.validatePasswordMatch(clientDto.getPassword(), clientDto.getConfirmPassword());

        userRepository.save(clientMapper.fromDto(clientDto));
    }
}
