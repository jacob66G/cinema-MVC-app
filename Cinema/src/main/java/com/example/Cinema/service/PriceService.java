package com.example.Cinema.service;

import com.example.Cinema.model.dto.PriceDto;
import com.example.Cinema.model.dto.PriceListDto;
import com.example.Cinema.model.Price;
import com.example.Cinema.model.enums.TicketCategory;
import com.example.Cinema.repository.PriceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public Price getPriceByType(TicketCategory type) {
        return priceRepository.getPriceByType(type);
    }

    public void updatePrices(PriceListDto priceListDto) {
        for (PriceDto price : priceListDto.getPriceList()) {
            Price priceToUpdate = getPriceByType(price.getType());
            priceToUpdate.setPriceValue(price.getPriceValue());
            save(priceToUpdate);
        }
    }
}
