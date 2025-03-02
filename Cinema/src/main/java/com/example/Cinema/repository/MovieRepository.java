package com.example.Cinema.repository;

import com.example.Cinema.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    @Query("SELECT COUNT(m) > 0 FROM Movie m WHERE m.title = :title AND (:idmovie IS NULL OR m.idmovie != :idmovie)")
    Boolean existsByTitle(String title, Long idmovie);
}
