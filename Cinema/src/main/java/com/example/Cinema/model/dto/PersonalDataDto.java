package com.example.Cinema.model.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PersonalDataDto {

    @NotBlank(message = "Imię jest wymagane")
    @Pattern(regexp = "^[a-zA-Ząęćżźńłóś]+$", message = "Użyto nieprawidłowych znaków")
    private String name;

    @NotBlank(message = "Nazwisko jest wymagane")
    @Pattern(regexp = "^[a-zA-Ząęćżźńłóś]+$", message = "Użyto nieprawidłowych znaków")
    private String surname;

    @NotBlank(message = "Numer telefonu jest wymagany")
    @Pattern(regexp = "^[0-9]+$", message = "Użyto nieprawidłowych znaków")
    @Size(min = 9, max = 9, message = "Numer powinien zawierać 9 cyfr")
    private String phone;
}
