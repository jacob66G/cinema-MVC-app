package com.example.Cinema.model.dto;

import com.example.Cinema.model.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ClientDto {

    @NotBlank(message = "Imie jest wymagane")
    @Pattern(regexp = "^$|^[a-zA-Ząęćżźńłóś]+$", message = "Użyto nieprawidłowych znaków")
    private String name;

    @NotBlank(message = "Nazwisko jest wymagane")
    @Pattern(regexp = "^$|^[a-zA-Ząęćżźńłóś]+$", message = "Użyto nieprawidłowych znaków")
    private String surname;

    @NotBlank(message = "Numer telefonu jest wymagany")
    @Pattern(regexp = "^$|^[0-9]+$", message = "Użyto nieprawidłowych znaków")
    @Size(min=9, max = 9,message = "numer powinnien zawierać 9 cyfr")
    private String phone;

    @NotBlank(message = "Adres email jest wymagany")
    @Pattern(regexp = "^$|^[a-zA-Ząęćżźńłóś0-9.-@]+$", message = "Użyto nieprawidłowych znaków")
    private String email;

    @NotBlank(message = "Wprowadź hasło")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*\\d)[a-zA-Ząęćżźńłóś0-9.-@]+$",
            message = "Hasło musi zawierać co najmniej jedną dużą literę i jedną cyfrę"
    )
    @Size(min=5 ,message = "Haslo nie moze być krótsze niż 5 znaków")
    private String password;

    @NotBlank(message = "Potwierdź hasło")
    private String confirmPassword;

    public ClientDto(User user) {
        this.name = user.getName();
        this.surname = user.getSurname();
        this.phone = user.getPhone();
        this.email = user.getUserName();
        this.password = user.getPassword();
    }
}
