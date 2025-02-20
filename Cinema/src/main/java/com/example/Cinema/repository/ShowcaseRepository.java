package com.example.Cinema.repository;


import com.example.Cinema.model.Showcase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ShowcaseRepository extends JpaRepository<Showcase, Long> {

}
