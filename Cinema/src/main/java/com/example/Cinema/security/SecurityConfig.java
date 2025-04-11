package com.example.Cinema.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;

@Configuration
public class SecurityConfig {

    private final UserDetailsService userDetailsService;

    public SecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());

        return new ProviderManager(List.of(authProvider));
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(configurer ->
                        configurer
                                .requestMatchers("/admin/**").hasRole("ADMIN")
                                .requestMatchers("/user/**").authenticated()
                                .requestMatchers("/h2-console/**").permitAll()
                                .requestMatchers("/mainpage").permitAll()
                                .requestMatchers("/pricelist").permitAll()
                                .requestMatchers("/programme").permitAll()
                                .requestMatchers("/reservation/**").permitAll()
                                .requestMatchers("/registration").permitAll()
                                .requestMatchers("/css/**", "/images/**").permitAll()
                                .anyRequest().authenticated()
                )
                .csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**")) // Wyłączenie CSRF dla H2
                .headers(headers -> headers.frameOptions(frame -> frame.disable())) // Zezwolenie na iframe
                .formLogin(form ->
                        form
                                .loginPage("/login")
                                .loginProcessingUrl("/authenticateTheUser")
                                .permitAll()
                                .successHandler((request, response, authentication) -> {
                                    if(authentication.getAuthorities().stream()
                                            .anyMatch(grantedAuthority ->
                                                    grantedAuthority.getAuthority().equals("ROLE_ADMIN"))) {
                                        response.sendRedirect("/admin");
                                    }
                                    else {
                                        response.sendRedirect("/mainpage");
                                    }
                                })
                )
                .logout(LogoutConfigurer::permitAll)
                .exceptionHandling(exception ->
                        exception.accessDeniedPage("/access-denied"));

        return http.build();
    }
}
