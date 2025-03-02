package com.example.Cinema.service.PDFGenerator;

import com.example.Cinema.model.Reservation;
import com.example.Cinema.model.Ticket;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class PdfGenerator {
    public void export (HttpServletResponse response, Reservation reservation) throws DocumentException, IOException {
        Document document = new Document();
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();

        document.add(new Paragraph("Rrezerwacja"));

        document.add(new Paragraph("Imie: " + reservation.getClientName()));
        document.add(new Paragraph("Nazwisko: " + reservation.getClientSurname()));
        document.add(new Paragraph("Email: " + reservation.getClientAddressEmail()));
        document.add(new Paragraph("Numer telefonu: " + reservation.getClientPhoneNumber()));

        document.add(new Paragraph("Bilety:"));
        List<Ticket> tickets = reservation.getTickets();
        for (Ticket ticket : tickets) {
            document.add(new Paragraph("Bilet nr: " + ticket.getIdticket()));
            document.add(new Paragraph("Miejsce: " + ticket.getSeat().getRow() + " - " + ticket.getSeat().getNumber()));
            document.add(new Paragraph("Typ biletu: " + ticket.getTicketType()));
        }

        document.add(new Paragraph("Cena: " + reservation.getPrice() + " PLN"));

        document.close();
    }
}
