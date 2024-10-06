package com.example.Cinema.service;

import com.example.Cinema.model.PriceList;
import com.example.Cinema.repository.PriceListRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;

@Service
public class PriceListService {

    private final PriceListRepository priceListRepository;

    @Autowired
    public PriceListService(PriceListRepository priceListRepository) {
        this.priceListRepository = priceListRepository;
    }

    public void save(PriceList priceList) {
        priceListRepository.save(priceList);
    }

    public void saveData(String ticketType, boolean weekendDay, Double price) {
        PriceList existingPriceList = priceListRepository.findByTicketTypeAndWeekendDay(ticketType, weekendDay);

        if(existingPriceList != null){
            existingPriceList.setPrice(price);
            priceListRepository.save(existingPriceList);
        }
        else {
            PriceList newPriceList = new PriceList();
            newPriceList.setTicketType(ticketType);
            newPriceList.setWeekendDay(weekendDay);
            newPriceList.setPrice(price);
            priceListRepository.save(newPriceList);
        }

    }

    public Double getTicketPrice(String ticketType, LocalDate date) {
        boolean weekendDay = false;
        if(date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY){
            weekendDay = true;
        }
        return priceListRepository.getPrice(ticketType, weekendDay);
    }

    public Double getTicketPrice(String ticketType, Boolean weekendDay) {

        return priceListRepository.getPrice(ticketType, weekendDay);
    }
}
