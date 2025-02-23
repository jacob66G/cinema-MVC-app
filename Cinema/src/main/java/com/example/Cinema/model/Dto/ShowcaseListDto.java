package com.example.Cinema.model.Dto;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ShowcaseListDto {

    @Valid
    private List<ShowcaseDto> showcases = new ArrayList<>();
}
