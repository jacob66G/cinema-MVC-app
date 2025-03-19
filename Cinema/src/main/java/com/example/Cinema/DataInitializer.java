package com.example.Cinema;

import com.example.Cinema.model.*;
import com.example.Cinema.repository.UserRepository;
import com.example.Cinema.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Component
public class DataInitializer implements CommandLineRunner {

    private final MovieService movieService;
    private final ProgrammeService programmeService;
    private final CinemaHallService cinemaHallService;
    private final PriceService priceService;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public DataInitializer(
            MovieService movieService,
            ProgrammeService programmeService,
            CinemaHallService cinemaHallService,
            PriceService priceService, UserRepository userRepository, BCryptPasswordEncoder passwordEncoder1
    ) {
        this.movieService = movieService;
        this.programmeService = programmeService;
        this.cinemaHallService = cinemaHallService;
        this.priceService = priceService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder1;
    }


    @Override
    public void run(String... args) throws Exception {

        User user = new User();
        user.setUserName("admin");
        user.setPassword(passwordEncoder.encode("admin"));
        user.setRole("ADMIN");

        userRepository.save(user);

        User client = new User();
        client.setName("Jan");
        client.setSurname("Kowalski");
        client.setPassword(passwordEncoder.encode("Jan123"));
        client.setUserName("jk@gmail.com");
        client.setPhone("111222333");
        client.setRole("CLIENT");

        userRepository.save(client);

        CinemaHall cinemaHallA = new CinemaHall("A");
        CinemaHall cinemaHallB = new CinemaHall("B");
        CinemaHall cinemaHallC = new CinemaHall("C");

        List<CinemaHall> cinemaHallList = new ArrayList<>(Arrays.asList(cinemaHallA, cinemaHallB, cinemaHallC));
        String rows[] = {"A", "B", "C", "D", "E", "F", "G", "H"};

        for(CinemaHall cinemaHall : cinemaHallList) {
            List<Seat> seats = new ArrayList<>();
            for (String row : rows) {
                for (int j = 1; j <= 14; j++) {

                    Seat seat = new Seat(row, j, cinemaHall);
                    seats.add(seat);
                }
            }
            cinemaHall.setSeats(seats);
            cinemaHallService.save(cinemaHall);
        }

        Price priceList1 = new Price("Normalny",25.0);
        Price priceList2 = new Price("Ulgowy",20.0);
        Price priceList3 = new Price("Studencki",30.0);
        Price priceList4 = new Price("Senior",25.0);

        priceService.save(priceList1);
        priceService.save(priceList2);
        priceService.save(priceList3);
        priceService.save(priceList4);


        Movie movie1 = new Movie(
                "Diuna: Część druga",
                "Książę Paul Atryda przyjmuje przydomek Muad'Dib i rozpoczyna duchowo-fizyczną podróż," +
                        " by stać się przepowiedzianym w proroctwie wyzwolicielem ludu Diuny.",
                loadImage("static/images/movies/diuna.jpg"),
                160);
        Movie movie2 = new Movie(
                "Avatar: Istota wody",
        "Pandorę znów napada wroga korporacja w poszukiwaniu cennych minerałów. Jack i Neytiri wraz z rodziną zmuszeni są opuścić" +
                " wioskę i szukać pomocy u innych plemion zamieszkujących planetę.",
                loadImage("static/images/movies/avatar.jpg"),
                180);
        Movie movie3 = new Movie(
                "Piraci z Karaibów",
                "Kowal Will Turner sprzymierza się z kapitanem Jackiem Sparrowem, by odzyskać swoją miłość - porwaną córkę gubernatora.",
                loadImage("static/images/movies/piraci-z-karaibow.jpg"),
                90);
        Movie movie4 = new Movie(
                "Królestwo Planety Małp",
                "W świecie zdominowanym przez małpy ludzie zostali zepchnięci na margines. Młody samiec wyrusza w trudną podróż," +
                        " która zmieni jego wyobrażenie o przeszłości i wpłynie na przyszłość małp i ludzi.",
                loadImage("static/images/movies/krolestwo-planety-malp.jpg"),
                140
        );
        Movie movie5 = new Movie(
                "V jak Vendetta",
                "W totalitarnej Anglii jedyną osobą walczącą o wolność jest bojownik przebrany za Guya Fawkesa – V," +
                        " który pewnego dnia uwalnia z rąk agentów rządowych młodą kobietę.",
                loadImage("static/images/movies/vjakvendetta.jpg"),
                140
        );
        Movie movie6 = new Movie(
                "Siedem",
                "Dwóch policjantów stara się złapać seryjnego mordercę wybierającego swoje ofiary według specjalnego klucza" +
                        " - siedmiu grzechów głównych.",
                loadImage("static/images/movies/siedem.jpg"),
                140
        );
        Movie movie7 = new Movie(
                "W pogoni za szczęściem",
                "Film oparty na faktach. Chris, samotny ojciec pozbawiony domu," +
                        " stara się mimo przeciwności losu o posadę w biurze maklerskim.",
                loadImage("static/images/movies/wpogonizaszczesciem.jpg"),
                140
        );

        movieService.save(movie1);
        movieService.save(movie2);
        movieService.save(movie3);
        movieService.save(movie4);
        movieService.save(movie5);
        movieService.save(movie6);
        movieService.save(movie7);

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
    private byte[] loadImage(String path) throws IOException {
        ClassPathResource imgFile = new ClassPathResource(path);
        return Files.readAllBytes(imgFile.getFile().toPath());
    }
}
