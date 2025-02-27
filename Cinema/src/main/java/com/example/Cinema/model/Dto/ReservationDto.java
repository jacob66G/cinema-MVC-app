package com.example.Cinema.model.Dto;

import com.example.Cinema.model.Programme;
import com.example.Cinema.model.Ticket;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ReservationDto {

    @NotBlank(message = "Imie jest wymagane")
    @Pattern(regexp = "^$|^[a-zA-Ząęćżźńłóś]+$", message = "Użyto nieprawidłowych znaków")
    private String clientName;

    @NotBlank(message = "Nazwisko jest wymagane")
    @Pattern(regexp = "^$|^[a-zA-Ząęćżźńłóś]+$", message = "Użyto nieprawidłowych znaków")
    private String clientSurname;

    @NotBlank(message = "Adres email jest wymagany")
    @Pattern(regexp = "^$|^[a-zA-Ząęćżźńłóś0-9.-@]+$", message = "Użyto nieprawidłowych znaków")
    private String clientAddressEmail;

    @NotBlank(message = "Potwierdz adres email")
    @Pattern(regexp = "^$|^[a-zA-Ząęćżźńłóś0-9.-@]+$", message = "Użyto nieprawidłowych znaków")
    private String confirmedClientAddressEmail;

    @NotBlank(message = "Numer telefonu jest wymagany")
    @Pattern(regexp = "^$|^[0-9]+$", message = "Użyto nieprawidłowych znaków")
    @Size(min=9, max = 9,message = "numer powinnien zawierać 9 cyfr")
    private String clientPhoneNumber;

    private List<Ticket> tickets;

    private LocalDateTime reservationDate;

    private Programme programme;

    private double totalPrice;
}
