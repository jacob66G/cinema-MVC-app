package com.example.Cinema.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PasswordChangeDto {

    @NotBlank(message = "Potwierdź hasło")
    private String confirmPassword;

    @Size(min=5 ,message = "Haslo nie moze być krótsze niż 5 znaków")
    private String newPassword;

    @NotBlank(message = "Potwierdź hasło")
    private String currentPassword;
}
