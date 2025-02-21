package com.example.Cinema.controller;

import com.example.Cinema.model.Price;
import com.example.Cinema.service.PriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/pricelist")
public class PriceListController {

    private final PriceService priceService;

    @Autowired
    public PriceListController(PriceService priceService) {
        this.priceService = priceService;
    }

    @GetMapping
    public String getPriceList(Model model) {
        List<Price> priceList = priceService.getPriceList();

        model.addAttribute("priceList", priceList);
        return "pricelist";
    }
}
