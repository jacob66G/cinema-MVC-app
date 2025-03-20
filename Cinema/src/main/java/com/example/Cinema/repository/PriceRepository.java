package com.example.Cinema.repository;

import com.example.Cinema.model.Price;
import com.example.Cinema.model.enums.TicketCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PriceRepository extends JpaRepository<Price, Long> {

    @Query("SELECT p FROM Price p  WHERE p.type = :type")
    Price getPriceByType(@Param("type") TicketCategory type);
}
