package com.example.Cinema.service.Validators;

import com.example.Cinema.exception.ValidationException;
import com.example.Cinema.model.User;
import com.example.Cinema.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserValidationService {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;

    public UserValidationService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public void validatePasswordMatch(String password, String confirmPassword) {
        if(!password.equals(confirmPassword)) {
            throw new ValidationException("Hasła różnią się");
        }
    }

    public void validateEmailMatch(String email, String confirmedEmail) {
        if(!email.equals(confirmedEmail)) {
            throw new ValidationException("Adresy emial różnią się");
        }
    }

    public void validateOldPassword(User user, String currentPassword) {
        if (!bCryptPasswordEncoder.matches(currentPassword, user.getPassword())) {
            throw new ValidationException("Niepoprawne aktualne hasło.");
        }
    }

    public void validateExistsByEmail(String email) {
        if(userRepository.findByUserName(email) != null) {
            throw new ValidationException("Uzytkownik o adresie email: " + email + " juz istnieje");
        }
    }

    public void validateExistsByPhone(String phone, long id) {
        if(userRepository.findByPhoneAndIdIsNot(phone, id) != null) {
            throw new ValidationException("Uzytkownik o numerze tel. : " + phone + " juz istnieje");
        }
    }

    public void validateExistsByPhone(String phone) {
        if(userRepository.findByPhone(phone) != null) {
            throw new ValidationException("Uzytkownik o numerze tel. : " + phone + " juz istnieje");
        }
    }
}
