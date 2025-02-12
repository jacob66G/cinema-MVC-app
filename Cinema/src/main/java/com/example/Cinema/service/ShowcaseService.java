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

    public List<Showcase> getAllShowcases() {
        return showcaseRepository.findAll();
    }

    public void save(Showcase showcase) {
        showcaseRepository.save(showcase);
    }

    public void editShowcase(Showcase showcaseToEdit) {

        Optional<Showcase> existingShowcase = showcaseRepository.findById(showcaseToEdit.getIdShowcase());

        if(existingShowcase.isPresent()){
            if(!showcaseToEdit.getTitle().isEmpty()){
                existingShowcase.get().setTitle(showcaseToEdit.getTitle());
            }
            if(!showcaseToEdit.getImageAddress().isEmpty()){
                existingShowcase.get().setImageAddress(showcaseToEdit.getImageAddress());
            }
            if(!showcaseToEdit.getType().isEmpty()){
                existingShowcase.get().setType(showcaseToEdit.getType());
            }

            showcaseRepository.save(existingShowcase.get());
        }
    }
}
