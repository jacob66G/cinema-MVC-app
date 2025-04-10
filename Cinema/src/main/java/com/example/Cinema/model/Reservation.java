package com.example.Cinema.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime reservationDate;
    private String clientName;
    private String clientSurname;
    private String clientAddressEmail;
    private String clientPhoneNumber;

    @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ticket> tickets;

    @ManyToOne()
    @JoinColumn(name = "user_id", nullable = true)
    private User user;

    public Reservation(
            LocalDateTime reservationDate,
            String clientName,
            String clientSurname,
            String clientAddressEmail,
            String clientPhoneNumber
    ) {
        this.reservationDate = reservationDate;
        this.clientName = clientName;
        this.clientSurname = clientSurname;
        this.clientAddressEmail = clientAddressEmail;
        this.clientPhoneNumber = clientPhoneNumber;
    }
}
