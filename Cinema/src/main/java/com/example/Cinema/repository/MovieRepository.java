package com.example.Cinema.repository;

import com.example.Cinema.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    @Query("SELECT COUNT(m) > 0 FROM Movie m WHERE m.title = :title AND (:id IS NULL OR m.id != :id)")
    Boolean existsByTitle(@Param("title") String title,@Param("id") Long id);

    @Query("SELECT m FROM Movie m WHERE LOWER(m.title) LIKE LOWER(CONCAT('%', :title, '%'))")
    List<Movie> findMovieByTitle(@Param("title") String title);
}
