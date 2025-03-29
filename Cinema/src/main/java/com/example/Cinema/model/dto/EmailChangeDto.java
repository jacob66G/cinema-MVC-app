package com.example.Cinema.model.dto;
import jakarta.validation.constraints.NotBlank;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EmailChangeDto {

    @NotBlank(message = "Adres email jest wymagany")
    @Pattern(regexp = "^[a-zA-Ząęćżźńłóś0-9.-@]+$", message = "Użyto nieprawidłowych znaków")
    private String email;

    @NotBlank(message = "Potwierdź email")
    @Pattern(regexp = "^[a-zA-Ząęćżźńłóś0-9.-@]+$", message = "Użyto nieprawidłowych znaków")
    private String confirmEmail;
}
