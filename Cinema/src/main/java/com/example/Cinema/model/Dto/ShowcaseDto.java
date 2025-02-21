package com.example.Cinema.model.Dto;

import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import org.springframework.web.multipart.MultipartFile;

@NoArgsConstructor
@Getter
@Setter
public class ShowcaseDto {

    private Long idShowcase;

    @NotBlank(message = "Tytuł jest wymagany")
    @Size(min = 2, max = 30, message = "nieprawidłowa długość tekstu")
    @Pattern(regexp = "^[a-zA-ZĄąĆćĘęŁłŃńÓóŚśŹźŻż0-9 ,:]+$", message = "nieprawidłowe znaki")
    private String title;

    @NotBlank(message = "Typ jest wymagany")
    @Size(min = 2, max=12, message = "nieprawidłowa długość tekstu")
    @Pattern(regexp = "^[a-zA-ZĄąĆćĘęŁłŃńÓóŚśŹźŻż ,:]+$", message = "nieprawidłowe znaki")
    private String type;

    private String base64Image;

    @Transient
    @NotNull(message = "Zdjęcie jest wymagane.")
    private MultipartFile image;

    public ShowcaseDto(Long idShowcase, String type, String title, String base64Image) {
        this.idShowcase = idShowcase;
        this.title = title;
        this.type = type;
        this.base64Image = base64Image;
    }

}
