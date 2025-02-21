package com.example.Cinema.service;

import com.example.Cinema.model.Price;
import com.example.Cinema.repository.PriceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class PriceService {

    private final PriceRepository priceRepository;

    @Autowired
    public PriceService(PriceRepository priceRepository) {
        this.priceRepository = priceRepository;
    }

    public List<Price> getPriceList() {
        return priceRepository.findAll();
    }
    public void save(Price price) {
        priceRepository.save(price);
    }

    public Double getTicketPrice(String ticketType, LocalDate date) {
        return null;
    }

    public Price getPriceByType(String type) {
        return priceRepository.getPriceByType(type);
    }
}
