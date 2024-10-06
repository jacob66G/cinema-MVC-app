package com.example.Cinema.controller;

import com.example.Cinema.model.PriceList;
import com.example.Cinema.service.PriceListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/pricelist")
public class PriceListController {

    private final PriceListService priceListService;

    @Autowired
    public PriceListController(PriceListService priceListService) {
        this.priceListService = priceListService;
    }

    @GetMapping
    public String getPriceListPage(Model model) {

        Double priceNormal = priceListService.getTicketPrice("Normalny", false);
        Double priceConcessionary = priceListService.getTicketPrice("Ulgowy", false);
        Double priceNormalWeekend =  priceListService.getTicketPrice("Normalny", true);
        Double priceConcessionaryWeekend = priceListService.getTicketPrice("Ulgowy", true);

        if(priceNormal == null){
            priceNormal = 0.0;
        } else if (priceConcessionary == null) {
            priceConcessionary = 0.0;
        } else if (priceNormalWeekend == null) {
            priceNormalWeekend = 0.0;
        } else if (priceConcessionaryWeekend == null) {
            priceConcessionaryWeekend =0.0;
        }

        model.addAttribute("priceNormal", priceNormal);
        model.addAttribute("priceConcessionary",priceConcessionary);
        model.addAttribute("priceNormalWeekend", priceNormalWeekend);
        model.addAttribute("priceConcessionaryWeekend",priceConcessionaryWeekend);

        return "pricelist";
    }
}
