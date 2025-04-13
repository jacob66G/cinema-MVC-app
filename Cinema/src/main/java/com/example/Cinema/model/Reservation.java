package com.example.Cinema.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="programme_id", nullable = false)
    private Programme programme;

    private LocalDateTime reservationDate;
    private String clientName;
    private String clientSurname;
    private String clientAddressEmail;
    private String clientPhoneNumber;
    private String code;

    @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ticket> tickets;

    @ManyToOne()
    @JoinColumn(name = "user_id", nullable = true)
    private User user;

    public void addTicket(Ticket ticket) {
        if(tickets == null) {
            tickets = new ArrayList<>();
        }

        tickets.add(ticket);
        ticket.setReservation(this);
    }
}
