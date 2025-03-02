package com.example.Cinema.Mapper;

import com.example.Cinema.model.Dto.ShowcaseDto;
import com.example.Cinema.model.Showcase;
import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
public class ShowcasesMapper {

    public ShowcaseDto toDto(Showcase showcase) {
        String base64Image = Base64.getEncoder().encodeToString(showcase.getImageData());

        return new ShowcaseDto(
                showcase.getIdShowcase(),
                showcase.getType(),
                showcase.getTitle(),
                base64Image
        );
    }
}
