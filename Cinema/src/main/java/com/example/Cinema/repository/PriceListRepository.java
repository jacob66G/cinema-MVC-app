package com.example.Cinema.repository;

import com.example.Cinema.model.PriceList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface PriceListRepository extends JpaRepository<PriceList, Long> {

    @Query("SELECT p.price FROM PriceList p WHERE p.ticketType = :ticketType AND p.weekendDay = :weekendDay")
    Double getPrice(@Param("ticketType") String ticketType, @Param("weekendDay") boolean weekendDay);

    PriceList findByTicketTypeAndWeekendDay(String ticketType, Boolean weekendDay);
}
