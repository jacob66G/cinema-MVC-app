package com.example.Cinema.model.Dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PriceDto {

    private long id;

    @NotBlank(message = "Typ ceny jest wymagany")
    private String type;

    @NotNull(message = "Cena jest wymagana")
    @Min(value = 0, message = "Cena nie może być mniejsza niż 0")
    private Double priceValue;
}
