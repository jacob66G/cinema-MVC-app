package com.example.Cinema.service;

import com.example.Cinema.model.Dto.ShowcaseDto;
import com.example.Cinema.model.Dto.ShowcaseListDto;
import com.example.Cinema.model.Showcase;
import com.example.Cinema.repository.ShowcaseRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class ShowcaseService {

    private final ShowcaseRepository showcaseRepository;

    public ShowcaseService(ShowcaseRepository showcaseRepository) {
        this.showcaseRepository = showcaseRepository;
    }

    public List<Showcase> getShowcases() {
        return showcaseRepository.findAll();
    }

    public void save(Showcase showcase) {
        showcaseRepository.save(showcase);
    }

    public Showcase getShowcaseById(Long idShowcase) {
        Optional<Showcase> showcase = showcaseRepository.findById(idShowcase);

        return showcase.orElse(null);
    }

    public void updateShowcasesDto(ShowcaseListDto showcaseListDto) {
        for(ShowcaseDto showcaseDto : showcaseListDto.getShowcases()) {
            Showcase showcase = getShowcaseById(showcaseDto.getIdShowcase());
            showcase.setType(showcaseDto.getType());
            showcase.setTitle(showcaseDto.getTitle());

            if(showcaseDto.getImage() != null && !showcaseDto.getImage().isEmpty()) {
                try {
                    byte[] imageData = showcaseDto.getImage().getBytes();
                    showcase.setImageData(imageData);
                } catch (IOException e) {
                    throw new RuntimeException("Błąd wczytywania obrazu", e);
                }
            }
           save(showcase);
        }
    }
}
