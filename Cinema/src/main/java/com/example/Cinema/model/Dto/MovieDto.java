package com.example.Cinema.model.Dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class MovieDto {
    private Long idmovie;

    @NotBlank(message = "Tytuł jest wymagany")
    @Pattern(regexp = "^$|^[a-zA-ZĄąĆćĘęŁłŃńÓóŚśŹźŻż0-9 ,:]+$", message = "nieprawidłowe znaki")
    private String title;

    @NotBlank(message = "Opis jest wymagany")
    @Pattern(regexp = "^$|^[a-zA-ZĄąĆćĘęŁłŃńÓóŚśŹźŻż0-9 ,:'-.]+$", message = "nieprawidłowe znaki")
    private String description;

    @NotNull(message = "Czas filmu jest wymagany")
    @Min(value = 0, message = "musi byc większy lub równy 0")
    private Integer duration;

    private String base64Image;

    private MultipartFile image;

    public MovieDto(Long idmovie, String title, String description, Integer duration, String base64Image) {
        this.idmovie = idmovie;
        this.title = title;
        this.description = description;
        this.duration = duration;
        this.base64Image = base64Image;
    }
}
