package com.example.Cinema.mapper;

import com.example.Cinema.model.User;
import com.example.Cinema.model.dto.ClientDto;
import com.example.Cinema.model.enums.UserRole;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class ClientMapper {

    private final BCryptPasswordEncoder passwordEncoder;

    public ClientMapper(BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public User fromDto(ClientDto dto) {
        User user = new User();
        user.setRole(UserRole.CLIENT);
        user.setName(dto.getName());
        user.setUserName(dto.getEmail());
        user.setPhone(dto.getPhone());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        return user;
    }
}
