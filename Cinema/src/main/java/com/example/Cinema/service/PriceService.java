package com.example.Cinema.service;

import com.example.Cinema.model.Dto.PriceDto;
import com.example.Cinema.model.Dto.PriceListDto;
import com.example.Cinema.model.Price;
import com.example.Cinema.model.Ticket;
import com.example.Cinema.repository.PriceRepository;
import jakarta.validation.Valid;
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

    public List<Price> getPrices() {
        return priceRepository.findAll();
    }
    public void save(Price price) {
        priceRepository.save(price);
    }

    public Price getPriceByType(String type) {
        return priceRepository.getPriceByType(type);
    }

    public double calculateTotalPrice(List<Ticket> tickets) {
        List<Price> prices = getPrices();

        tickets.forEach(ticket -> prices.stream()
                    .filter(price -> price.getType().equalsIgnoreCase(ticket.getTicketType()))
                    .findFirst()
                    .ifPresent(price -> ticket.setPrice(price.getPriceValue())
                )
        );

        return tickets.stream().mapToDouble(Ticket::getPrice).sum();
    }

    public void updatePrieces(PriceListDto priceListDto) {
        for (PriceDto price : priceListDto.getPriceList()) {
            Price priceToUpdate = getPriceByType(price.getType());
            priceToUpdate.setPriceValue(price.getPriceValue());
            save(priceToUpdate);
        }
    }
}
