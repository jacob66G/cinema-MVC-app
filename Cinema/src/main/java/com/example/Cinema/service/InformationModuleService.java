package com.example.Cinema.service;

import com.example.Cinema.model.InformationModule;
import com.example.Cinema.repository.InformationModuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class InformationModuleService {

    private final InformationModuleRepository informationModuleRepository;

    @Autowired
    public InformationModuleService(InformationModuleRepository informationModuleRepository) {
        this.informationModuleRepository = informationModuleRepository;
    }

    public void save(InformationModule module){
        informationModuleRepository.save(module);
    }

    public void editInformationsModule(String type, String title, String image, String review, String description){

        Optional<InformationModule> existingModule = informationModuleRepository.findByType(type);

        if(existingModule.isPresent()){
            if(!title.isEmpty()){
                existingModule.get().setTitle(title);
            }
            if(!image.isEmpty()){
                existingModule.get().setImageAddress(image);
            }
            if(!review.isEmpty()){
                existingModule.get().setReviewAddress(review);
            }
            if(!description.isEmpty()){
                existingModule.get().setDescription(description);
            }

            informationModuleRepository.save(existingModule.get());
        }
        else {
            InformationModule newModule = new InformationModule(type, title, description, image, review);
            informationModuleRepository.save(newModule);
        }
    }
}
