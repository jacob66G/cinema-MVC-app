package com.example.Cinema.controller;

import com.example.Cinema.model.Movie;
import com.example.Cinema.service.PriceListService;
import org.springframework.ui.Model;
import com.example.Cinema.service.InformationModuleService;
import com.example.Cinema.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;


@Controller
@RequestMapping("/admin/mainpage")
public class AdminMainPageController {

    private final InformationModuleService informationModuleService;
    private final MovieService movieService;
    private final PriceListService priceListService;

    @Autowired
    public AdminMainPageController(InformationModuleService informationModuleService, MovieService movieService, PriceListService priceListService) {
        this.informationModuleService = informationModuleService;
        this.movieService = movieService;
        this.priceListService = priceListService;
    }

    @GetMapping()
    public String mainPageEdit(Model model){
        List<Movie> movies = movieService.findAll();
        movies.sort(Comparator.comparing(Movie::getTitle));

        model.addAttribute("movies", movies);
        return "adminview/admin-page";
    }

    @PostMapping("/modules")
    public String editModules(
            @RequestParam(name = "newsTitle", required = false) String newsTitle,
            @RequestParam(name = "newsImage", required = false) String newsImage,
            @RequestParam(name = "newsLink", required = false) String newsLink,
            @RequestParam(name = "newsDescription", required = false) String newsDescription,

            @RequestParam(name = "premiereTitle", required = false) String premiereTitle,
            @RequestParam(name = "premiereImage", required = false) String premiereImage,
            @RequestParam(name = "premiereLink", required = false) String premiereLink,
            @RequestParam(name = "premiereDescription", required = false) String premiereDescription,

            @RequestParam(name = "presentTitle", required = false) String presentTitle,
            @RequestParam(name = "presentImage", required = false) String presentImage,
            @RequestParam(name = "presentLink", required = false) String presentLink,
            @RequestParam(name = "presentDescription", required = false) String presentDescription,

            @RequestParam(name = "present2Title", required = false) String present2Title,
            @RequestParam(name = "present2Image", required = false) String present2Image,
            @RequestParam(name = "present2Link", required = false) String present2Link,
            @RequestParam(name = "present2Description", required = false) String present2Description,
            Model model
            ){

        informationModuleService.editInformationsModule("news", newsTitle, newsImage, newsLink, newsDescription);
        informationModuleService.editInformationsModule("premiere", premiereTitle, premiereImage, premiereLink, premiereDescription);
        informationModuleService.editInformationsModule("present", presentTitle, presentImage, presentLink, presentDescription);
        informationModuleService.editInformationsModule("present2", present2Title, present2Image, present2Link, present2Description);

        List<Movie> movies = movieService.findAll();
        model.addAttribute("movies", movies);

        return "adminview/admin-page";
    }

    @PostMapping("/pricelist")
    public String editPriceList(
            @RequestParam(name = "price-normal", required = false) Double priceNormal,
            @RequestParam(name = "price-concessionary", required = false) Double priceConcessionary,
            @RequestParam(name = "price-normal-weekend", required = false) Double priceNormalWeekend,
            @RequestParam(name = "price-concessionary-weekend", required = false) Double priceConcessionaryWeekend,
            Model model
            ){

        if(priceNormal !=null){
            priceListService.saveData("Normalny", false, priceNormal);
        }
        if (priceConcessionary != null) {
            priceListService.saveData("Ulgowy", false, priceConcessionary);
        }
        if (priceNormalWeekend != null) {
            priceListService.saveData("Normalny", true, priceNormalWeekend);
        }
        if (priceConcessionaryWeekend != null) {
            priceListService.saveData("Ulgowy", true, priceConcessionaryWeekend);
        }

        List<Movie> movies = movieService.findAll();
        model.addAttribute("movies", movies);

        return "adminview/admin-page";
    }

}

