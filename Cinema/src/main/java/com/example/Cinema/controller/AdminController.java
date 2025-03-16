package com.example.Cinema.controller;

import com.example.Cinema.model.Dto.PriceDto;
import com.example.Cinema.model.Price;
import com.example.Cinema.model.Dto.PriceListDto;
import com.example.Cinema.service.PriceService;
import jakarta.validation.Valid;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@RequestMapping("/admin")
public class AdminController {

    private final PriceService priceService;

    @Autowired
    public AdminController(PriceService priceService) {
        this.priceService = priceService;
    }

    @GetMapping()
    public String getAdminPage() {
        return "adminview/admin-page";
    }


    @GetMapping("/edit/pricelist")
    public String getEditPricesForm(Model model) {
        List<Price> priceList = priceService.getPrices();

        List<PriceDto> pricesDto = priceList.stream().map(price ->
                new PriceDto(price.getIdprice(), price.getType(), price.getPriceValue())
        ).toList();

        PriceListDto priceListDto = new PriceListDto(pricesDto);

        model.addAttribute("priceListDto", priceListDto);
        return "adminview/pricelist-form";
    }


    @PostMapping("/edit/pricelist")
    public String editPrices(
            @Valid @ModelAttribute("priceListDto") PriceListDto priceListDto,
            BindingResult theBindingResult,
            Model model
    ) {
        if(theBindingResult.hasErrors()) {
            model.addAttribute("priceListDto", priceListDto);
            return "adminview/pricelist-form";
        }

        priceService.updatePrieces(priceListDto);

        return "redirect:/admin";
    }
}

