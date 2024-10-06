package com.example.Cinema;

import com.example.Cinema.model.*;
import com.example.Cinema.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.time.LocalTime;


@Component
public class DataInitializer implements CommandLineRunner {

    private final MovieService movieService;
    private final ProgrammeService programmeService;
    private final InformationModuleService informationModuleService;
    private final CinemaHallService cinemaHallService;
    private final PriceListService priceListService;

    @Autowired
    public DataInitializer(MovieService movieService, ProgrammeService programmeService, InformationModuleService informationModuleService, CinemaHallService cinemaHallService, PriceListService priceListService) {
        this.movieService = movieService;
        this.programmeService = programmeService;
        this.informationModuleService = informationModuleService;
        this.cinemaHallService = cinemaHallService;
        this.priceListService = priceListService;
    }

    @Override
    public void run(String... args) throws Exception {

        CinemaHall cinemaHallA = new CinemaHall("A", 8, 20);
        CinemaHall cinemaHallB = new CinemaHall("B", 8, 18);
        CinemaHall cinemaHallC = new CinemaHall("C", 10, 20);

        cinemaHallService.save(cinemaHallA);
        cinemaHallService.save(cinemaHallB);
        cinemaHallService.save(cinemaHallC);

        PriceList priceList1 = new PriceList("Normalny", false, 25.0);
        PriceList priceList2 = new PriceList("Ulgowy", false, 20.0);
        PriceList priceList3 = new PriceList("Normalny", true, 30.0);
        PriceList priceList4 = new PriceList("Ulgowy", true, 25.0);

        priceListService.save(priceList1);
        priceListService.save(priceList2);
        priceListService.save(priceList3);
        priceListService.save(priceList4);

        InformationModule news = new InformationModule(
                "news",
                "Diuna: Część druga",
                "Książę Paul Atryda przyjmuje przydomek Muad'Dib i rozpoczyna duchowo-fizyczną podróż," +
                " by stać się przepowiedzianym w proroctwie wyzwolicielem ludu Diuny.",
                "https://fwcdn.pl/fpo/34/81/10003481/8115126_1.3.jpg",
                "https://www.filmweb.pl/film/Diuna%3A+Cz%C4%99%C5%9B%C4%87+druga-2024-10003481");
        InformationModule premiere = new InformationModule(
                "premiere",
                "Avatar: Istota wody",
                "Pandorę znów napada wroga korporacja w poszukiwaniu cennych minerałów. Jack i Neytiri wraz z rodziną zmuszeni są opuścić" +
                        " wioskę i szukać pomocy u innych plemion zamieszkujących planetę.",
                "https://fwcdn.pl/fpo/81/78/558178/8047434_1.3.jpg",
                "https://www.filmweb.pl/film/Avatar%3A+Istota+wody-2022-558178");
        InformationModule present = new InformationModule(
                "present",
                "Kruk",
                "Erick i jego dziewczyna zostają zamordowani. Rok później mężczyzna powraca z zaświatów, aby przy pomocy kruka pomścić śmierć ukochanej.",
                "https://fwcdn.pl/fpo/09/42/550942/8120460_2.8.webp",
                "https://www.filmweb.pl/film/Kruk-2024-550942");
        InformationModule present2 = new InformationModule(
                "present2",
                "Kapitan Ameryka: Nowy wspaniały świat",
                "Sam Wilson, który oficjalnie przyjął rolę Kapitana Ameryki, znajduje się w samym środku międzynarodowego incydentu.",
                "https://fwcdn.pl/fpo/31/93/873193/8135773.10.webp",
                "https://www.filmweb.pl/film/Kapitan+Ameryka%3A+Nowy+wspania%C5%82y+%C5%9Bwiat-2025-873193"
        );

        informationModuleService.save(news);
        informationModuleService.save(premiere);
        informationModuleService.save(present);
        informationModuleService.save(present2);

        Movie movie1 = new Movie(
                "Diuna: Część druga",
                "Książę Paul Atryda przyjmuje przydomek Muad'Dib i rozpoczyna duchowo-fizyczną podróż," +
                        " by stać się przepowiedzianym w proroctwie wyzwolicielem ludu Diuny.",
                "https://fwcdn.pl/fpo/34/81/10003481/8115126_1.3.jpg",
                160);
        Movie movie2 = new Movie(
                "Avatar: Istota wody",
        "Pandorę znów napada wroga korporacja w poszukiwaniu cennych minerałów. Jack i Neytiri wraz z rodziną zmuszeni są opuścić" +
                " wioskę i szukać pomocy u innych plemion zamieszkujących planetę.",
                "https://fwcdn.pl/fpo/81/78/558178/8047434_1.3.jpg",
                180);
        Movie movie3 = new Movie(
                "W głowie się nie mieści 2",
                "Riley jest już nastolatką. W jej życiu pojawiają się nowe emocje, które kształtują osobowość dorastającej dziewczyny.",
                "https://fwcdn.pl/fpo/97/16/10019716/8129820.3.jpg",
                90);
        Movie movie4 = new Movie(
                "Królestwo Planety Małp",
                "W świecie zdominowanym przez małpy ludzie zostali zepchnięci na margines. Młody samiec wyrusza w trudną podróż," +
                        " która zmieni jego wyobrażenie o przeszłości i wpłynie na przyszłość małp i ludzi.",
                "https://fwcdn.pl/fpo/88/22/848822/8128230.3.jpg",
                140
        );

        movieService.saveMovie(movie1);
        movieService.saveMovie(movie2);
        movieService.saveMovie(movie3);
        movieService.saveMovie(movie4);

        LocalDate dateCurrent = LocalDate.now();
        LocalDate date1 = dateCurrent.plusDays(1);
        LocalDate date2 = dateCurrent.plusDays(2);

        Programme programme1Movie1A = new Programme(movie1, dateCurrent, LocalTime.of(10,0), cinemaHallA);
        Programme programme2Movie1A = new Programme(movie1, dateCurrent, LocalTime.of(20,0), cinemaHallA);
        Programme programme3Movie1C = new Programme(movie1, dateCurrent, LocalTime.of(16,0), cinemaHallC);
        Programme programme4Movie1A = new Programme(movie1, date1, LocalTime.of(10,0), cinemaHallA);
        Programme programme5Movie1B = new Programme(movie1, date1, LocalTime.of(16,0), cinemaHallB);

        Programme programme6Movie2A = new Programme(movie2, dateCurrent, LocalTime.of(14,0), cinemaHallA);
        Programme programme7Movie2C = new Programme(movie2, dateCurrent, LocalTime.of(20,0), cinemaHallC);
        Programme programme8Movie2A = new Programme(movie2,date1, LocalTime.of(14,0), cinemaHallA);
        Programme programme9Movie2A = new Programme(movie2, date1, LocalTime.of(20,0), cinemaHallA);
        Programme programme10Movie2A = new Programme(movie2, date2, LocalTime.of(15,0), cinemaHallA);
        Programme programme11Movie2B = new Programme(movie2, date2, LocalTime.of(19,0), cinemaHallB);


        Programme programme12Movie3A = new Programme(movie3, dateCurrent, LocalTime.of(18,0), cinemaHallA);
        Programme programme13Movie3B = new Programme(movie3, dateCurrent, LocalTime.of(12,0), cinemaHallB);
        Programme programme14Movie3A = new Programme(movie3, date1, LocalTime.of(18,0), cinemaHallA);
        Programme programme16Movie3A = new Programme(movie3, date2, LocalTime.of(19,0), cinemaHallA);
        Programme programme17Movie3C = new Programme(movie3, date2, LocalTime.of(16,0), cinemaHallC);


        Programme programme18Movie4B = new Programme(movie4, date1, LocalTime.of(12,0), cinemaHallB);
        Programme programme19Movie4B = new Programme(movie4, date1, LocalTime.of(20,0), cinemaHallB);
        Programme programme20Movie4A = new Programme(movie4, date2, LocalTime.of(21,0), cinemaHallA);
        Programme programme21Movie4B = new Programme(movie4, date2, LocalTime.of(12,0), cinemaHallB);
        Programme programme22Movie4C = new Programme(movie4, date2, LocalTime.of(14,0), cinemaHallC);


        programmeService.save(programme1Movie1A);
        programmeService.save(programme2Movie1A);
        programmeService.save(programme3Movie1C);
        programmeService.save(programme4Movie1A);
        programmeService.save(programme5Movie1B);

        programmeService.save(programme6Movie2A);
        programmeService.save(programme7Movie2C);
        programmeService.save(programme8Movie2A);
        programmeService.save(programme9Movie2A);
        programmeService.save(programme10Movie2A);
        programmeService.save(programme11Movie2B);

        programmeService.save(programme12Movie3A);
        programmeService.save(programme13Movie3B);
        programmeService.save(programme14Movie3A);
        programmeService.save(programme16Movie3A);
        programmeService.save(programme17Movie3C);

        programmeService.save(programme18Movie4B);
        programmeService.save(programme19Movie4B);
        programmeService.save(programme20Movie4A);
        programmeService.save(programme21Movie4B);
        programmeService.save(programme22Movie4C);

    }
}
