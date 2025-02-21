package com.example.Cinema.controller;

import com.example.Cinema.model.*;
import com.example.Cinema.service.PriceService;
import com.example.Cinema.service.ProgrammeService;
import com.example.Cinema.service.ReservationService;
import com.example.Cinema.service.TicketService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Controller
@RequestMapping("/reservation")
public class ReservationController {

    private final ReservationService reservationService;
    private final ProgrammeService programmeService;
    private final TicketService ticketService;
    private final PriceService priceService;

    @Autowired
    public ReservationController(ReservationService reservationService, ProgrammeService programmeService, TicketService ticketService, PriceService priceService) {
        this.reservationService = reservationService;
        this.programmeService = programmeService;
        this.ticketService = ticketService;
        this.priceService = priceService;
    }
//
//    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE dd MMMM yyyy", new Locale("pl"));
//
//    @GetMapping("/{id}")
//    public String getReservationPage(@PathVariable Long id, Model model) {
//        Optional<Programme> programme = programmeService.findById(id);
//
//        if (programme.isPresent()) {
//            LocalDate date = programme.get().getDate();
//            LocalTime time = programme.get().getTime();
//            Movie movie = programme.get().getMovie();
//            CinemaHall cinemaHall = programme.get().getCinemaHall();
//
//
//            List<String> rows = new ArrayList<>();
//
//
//
//            List<String> bookedSeats = ticketService.getBookedSeats(programme.get());
//
//            String formattedDate = date.format(formatter);
//
//            model.addAttribute("movie", movie);
//            model.addAttribute("date", formattedDate);
//            model.addAttribute("time", time);
//            model.addAttribute("rowsList", rows);
//            model.addAttribute("programme", programme.get());
//            model.addAttribute("bookedSeats", bookedSeats);
//
//        }
//
//        return "reservation";
//    }
//
//    @PostMapping("/{id}")
//    public String makeReservation(
//            @PathVariable Long id,
//            @RequestParam(value = "selectedSeats", required = false) List<String> selectedSeats,
//            HttpSession session,
//            Model model
//            ) {
//
//        if(selectedSeats == null){
//            model.addAttribute("errorMessage", "Nie wybrano miejsca");
//            return getReservationPage(id, model);
//        }
//
//        Optional<Programme> programme = programmeService.findById(id);
//        Reservation reservation = new Reservation();
//        List<Ticket> ticketList = new ArrayList<>();
//
//        for (String seat : selectedSeats) {
//            Ticket ticket1 = new Ticket();
//            ticket1.setProgramme(programme.get());
//            ticket1.setSeat(seat);
//
//            ticketList.add(ticket1);
//        }
//
//        reservation.setTickets(ticketList);
//
//        session.setAttribute("reservation", reservation);
//        session.setAttribute("idprogramme", id);
//
//        return "redirect:/reservation/data";
//    }
//
//    @GetMapping("/data")
//    public String getReservationDataPage(Model model, HttpSession session) {
//
//        Reservation reservation = (Reservation) session.getAttribute("reservation");
//        Long idprogramme = (Long) session.getAttribute("idprogramme");
//
//        List<Ticket> ticketList = reservation.getTickets();
//
//        session.setAttribute("reservation", reservation);
//        session.setAttribute("ticketList", ticketList);
//
//        model.addAttribute("ticketList", ticketList);
//        model.addAttribute("idprogramme", idprogramme);
//
//        return "reservation-data";
//    }
//
//
//    @PostMapping("/data")
//    public String completeReservation(
//            @RequestParam("first-name") String name,
//            @RequestParam("last-name") String surname,
//            @RequestParam("phone-number") String phoneNumber,
//            @RequestParam("email") String email,
//            @RequestParam("confirm-email") String confirm_email,
//            @RequestParam Map<String, String> ticketsWithTypes,
//            Model model,
//            HttpSession session
//            ){
//
//        String errorMessage = reservationService.checkEmailAndPhoneNumber(phoneNumber, email, confirm_email);
//
//        if(errorMessage != null){
//            Long idprogramme = (Long) session.getAttribute("idprogramme");
//            List<Ticket> ticketList = (List<Ticket>) session.getAttribute("ticketList");
//
//            model.addAttribute("errorMessage", errorMessage);
//            model.addAttribute("ticketList", ticketList);
//            model.addAttribute("idprogramme", idprogramme);
//            return "reservation-data";
//        }
//
//
//        Reservation reservation = (Reservation) session.getAttribute("reservation");
//        reservation.setClientName(name);
//        reservation.setClientSurname(surname);
//        reservation.setClientPhoneNumber(phoneNumber);
//        reservation.setClientAddressEmail(email);
//        reservation.setReservationDate(LocalDateTime.now());
//
//        List<Ticket> ticketList = reservation.getTickets();
//
//        Programme programme = reservation.getTickets().get(0).getProgramme();
//        LocalDate programmeDate = programme.getDate();
//
//        for(Ticket ticket : ticketList){
//            String seat = ticket.getSeat();
//            String ticketType = ticketsWithTypes.get(seat);
//            ticket.setTicketType(ticketType);
//            ticket.setPrice(priceService.getTicketPrice(ticketType, programmeDate));
//            ticket.setReservation(reservation);
//        }
//
//
//        Double price = 0.0;
//
//        for(Ticket ticket : ticketList){
//            price += ticket.getPrice();
//        }
//
//        String date = programmeDate.format(formatter);
//
//        session.setAttribute("reservation", reservation);
//        session.setAttribute("programme", programme);
//        session.setAttribute("date", date);
//        session.setAttribute("time", programme.getTime());
//        session.setAttribute("price", price);
//
//        return "redirect:/reservation/summary";
//    }
//
//    @GetMapping("/summary")
//    public String getSummaryPage(Model model, HttpSession session){
//
//        Reservation reservation = (Reservation) session.getAttribute("reservation");
//        Programme programme = (Programme) session.getAttribute("programme");
//        String date = (String) session.getAttribute("date");
//        LocalTime time = (LocalTime) session.getAttribute("time");
//        Double price = (Double) session.getAttribute("price");
//
//        model.addAttribute("reservation", reservation);
//        model.addAttribute("programme", programme);
//        model.addAttribute("date", date);
//        model.addAttribute("time", time);
//        model.addAttribute("price", price);
//        return "summary";
//    }
//
//    @PostMapping("/summary")
//    public String confirmReservation(HttpSession session, Model model){
//
//        Reservation reservation = (Reservation)  session.getAttribute("reservation");
//
//        if(reservation != null){
//            reservationService.save(reservation);
//        }
//        else {
//            model.addAttribute("errorMessage", "Wystąpił nieoczekiwany błąd. Spróbuj ponownie");
//            return getSummaryPage(model, session);
//        }
//
//        return "redirect:/mainpage";
//    }
}