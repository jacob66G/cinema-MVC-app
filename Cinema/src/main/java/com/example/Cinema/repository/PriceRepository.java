package com.example.Cinema.repository;

import com.example.Cinema.model.Price;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface PriceRepository extends JpaRepository<Price, Long> {

    @Query("SELECT p.price FROM Price p WHERE p.type = :type")
    Double getPrice(@Param("type") String type);
}
