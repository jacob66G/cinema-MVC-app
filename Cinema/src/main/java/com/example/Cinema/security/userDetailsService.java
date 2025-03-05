package com.example.Cinema.security;

import com.example.Cinema.model.User;
import com.example.Cinema.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class userDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public userDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUserName(username);

        if(user == null) {
            throw new UsernameNotFoundException("Invalid username or password.");
        }

        return new UserDao(user);
    }

}
