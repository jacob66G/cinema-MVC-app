package com.example.Cinema.repository;

import com.example.Cinema.model.InformationModule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InformationModuleRepository extends JpaRepository<InformationModule, Long> {

    Optional<InformationModule> findByType(String moduleType);
}
