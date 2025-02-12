package com.example.Cinema.repository;


import com.example.Cinema.model.Showcase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShowcaseRepository extends JpaRepository<Showcase, Long> {

}
