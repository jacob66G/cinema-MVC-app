package com.example.Cinema.model.Dto;

import com.example.Cinema.model.Showcase;
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
public class ShowCaseListDto {

    @Valid
    private List<ShowcaseDto> showcases = new ArrayList<>();

    public void addShowCase(ShowcaseDto showcaseDto) {
        this.showcases.add(showcaseDto);
    }
}
