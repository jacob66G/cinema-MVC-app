package com.example.Cinema.controller;

import com.example.Cinema.model.Dto.PriceDto;
import com.example.Cinema.model.Dto.ShowcaseListDto;
import com.example.Cinema.model.Dto.ShowcaseDto;
import com.example.Cinema.model.Price;
import com.example.Cinema.model.Dto.PriceListDto;
import com.example.Cinema.model.Showcase;
import com.example.Cinema.service.PriceService;
import com.example.Cinema.service.ShowcaseService;
import jakarta.validation.Valid;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Base64;
import java.util.List;


@Controller
@RequestMapping("/admin")
public class AdminController {

    private final ShowcaseService showcaseService;
    private final PriceService priceService;

    @Autowired
    public AdminController(ShowcaseService showcaseService, PriceService priceService) {
        this.showcaseService = showcaseService;
        this.priceService = priceService;
    }

    @GetMapping()
    public String getAdminPage() {
        return "adminview/admin-page";
    }


    @GetMapping("/edit/showcases")
    public String getEditShowcasesForm(Model model) {
        List<Showcase> showcases = showcaseService.getShowcases();

        List<ShowcaseDto> showcaseDtos = showcases.stream().map(showcase -> {
            String base64Image = Base64.getEncoder().encodeToString(showcase.getImageData());
            return new ShowcaseDto(showcase.getIdShowcase(), showcase.getType(), showcase.getTitle(), base64Image);
        }).toList();

        ShowcaseListDto showcaseListDto = new ShowcaseListDto(showcaseDtos);

        model.addAttribute("showcaseListDto", showcaseListDto);
        return "adminview/showcases-form";
    }

    @PostMapping("/edit/showcases")
    public String editShowcases(@Valid @ModelAttribute("showcaseListDto") ShowcaseListDto showcaseListDto, BindingResult theBindingResult, Model model) throws IOException {


        if(theBindingResult.hasErrors()) {
            model.addAttribute("showcaseListDto", showcaseListDto);

            return "adminview/showcases-form";
        }
        else {
            for(ShowcaseDto showcase : showcaseListDto.getShowcases()) {

                Showcase showcaseToUpdate = showcaseService.getShowcaseById(showcase.getIdShowcase());
                showcaseToUpdate.setType(showcase.getType());
                showcaseToUpdate.setTitle(showcase.getTitle());

                if(showcase.getImage() != null && !showcase.getImage().isEmpty()) {
                    byte[] imageData = showcase.getImage().getBytes();
                    showcaseToUpdate.setImageData(imageData);
                }
                showcaseService.save(showcaseToUpdate);
            }

            return "redirect:/admin";
        }
    }

    @GetMapping("/edit/pricelist")
    public String getEditPricesForm(Model model) {
        List<Price> priceList = priceService.getPriceList();

        List<PriceDto> pricesDto = priceList.stream().map(price ->
                new PriceDto(price.getIdprice(), price.getType(), price.getPriceValue())
        ).toList();

        PriceListDto priceListDto = new PriceListDto(pricesDto);

        model.addAttribute("priceListDto", priceListDto);
        return "adminview/pricelist-form";
    }

    @PostMapping("/edit/pricelist")
    public String editPrices(@Valid @ModelAttribute("priceListDto") PriceListDto priceListDto, BindingResult theBindingResult, Model model) {

        if(theBindingResult.hasErrors()) {
            model.addAttribute("priceListDto", priceListDto);

            return "adminview/pricelist-form";
        }

        for (PriceDto price : priceListDto.getPriceList()) {
            Price priceToUpdate = priceService.getPriceByType(price.getType());
            priceToUpdate.setPriceValue(price.getPriceValue());
            priceService.save(priceToUpdate);
        }

        return "redirect:/admin";
    }
}

