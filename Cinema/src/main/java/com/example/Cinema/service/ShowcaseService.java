package com.example.Cinema.service;

import com.example.Cinema.model.Showcase;
import com.example.Cinema.repository.ShowcaseRepository;
import org.springframework.stereotype.Service;

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
}
