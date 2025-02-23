package com.example.Cinema.model.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import org.springframework.web.multipart.MultipartFile;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class ShowcaseDto {

    private Long idShowcase;

    @NotBlank(message = "Tytuł jest wymagany")
    @Pattern(regexp = "^[a-zA-ZĄąĆćĘęŁłŃńÓóŚśŹźŻż0-9 ,:]+$", message = "Nieprawidłowe znaki")
    private String title;

    @NotBlank(message = "Typ jest wymagany")
    @Pattern(regexp = "^[a-zA-ZĄąĆćĘęŁłŃńÓóŚśŹźŻż ,:]+$", message = "Nieprawidłowe znaki")
    private String type;

    private String base64Image;

    @NotNull(message = "Zdjęcie jest wymagane.")
    private MultipartFile image;

    public ShowcaseDto(Long idShowcase, String type, String title, String base64Image) {
        this.idShowcase = idShowcase;
        this.title = title;
        this.type = type;
        this.base64Image = base64Image;
    }

}
