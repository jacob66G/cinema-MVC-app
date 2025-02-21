package com.example.Cinema.controller;

import com.example.Cinema.model.Dto.PriceDto;
import com.example.Cinema.model.Dto.ShowCaseListDto;
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
    public String getAdminPage(Model model) {
        List<Price> priceList = priceService.getPriceList();
        List<PriceDto> pricesDto = priceList.stream().map(price ->
                new PriceDto(price.getIdprice(), price.getType(), price.getPriceValue())
        ).toList();

        PriceListDto priceListDto = new PriceListDto(pricesDto);

        List<Showcase> showcases = showcaseService.getShowcases();
        List<ShowcaseDto> showcaseDtos = showcases.stream().map(showcase -> {
            String base64Image = Base64.getEncoder().encodeToString(showcase.getImageData());
            return new ShowcaseDto(showcase.getIdShowcase(), showcase.getType(), showcase.getTitle(), base64Image);
        }).toList();

        ShowCaseListDto showCaseListDto = new ShowCaseListDto();
        showCaseListDto.getShowcases().addAll(showcaseDtos);

        model.addAttribute("formEditPriceList", priceListDto);
        model.addAttribute("formEditShowcases", showCaseListDto);

        return "adminview/admin-page";
    }

    @PostMapping("/edit/showcases")
    public String editShowcases(@Valid @ModelAttribute ShowCaseListDto showcaseListDto, BindingResult theBindingResult, Model model) {

        if(theBindingResult.hasErrors()) {
            model.addAttribute("formEditShowcases", showcaseListDto);

            System.out.println(theBindingResult.getAllErrors());
            return "adminview/admin-page";
        }
        else {
            for(ShowcaseDto showcase : showcaseListDto.getShowcases()) {
                try {
                    Showcase showcaseToUpdate = showcaseService.getShowcaseById(showcase.getIdShowcase());
                    showcaseToUpdate.setType(showcase.getType());
                    showcaseToUpdate.setTitle(showcase.getTitle());

                    if(showcase.getImage() != null && !showcase.getImage().isEmpty()) {
                        byte[] imageData = showcase.getImage().getBytes();
                        showcaseToUpdate.setImageData(imageData);
                    }

                    showcaseService.save(showcaseToUpdate);

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            return "redirect:/admin";
        }
    }

    @PostMapping("/edit/pricelist")
    public String editPrices(@ModelAttribute PriceListDto priceListDto) {

        for (PriceDto price : priceListDto.getPriceList()) {
            Price priceToUpdate = priceService.getPriceByType(price.getType());
            priceToUpdate.setPriceValue(price.getPriceValue());
            priceService.save(priceToUpdate);
        }

        return "redirect:/admin";
    }

}

