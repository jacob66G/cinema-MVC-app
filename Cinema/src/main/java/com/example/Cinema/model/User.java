package com.example.Cinema.model;

import com.example.Cinema.model.enums.UserRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String userName;
    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    private String name;
    private String surname;
    private String phone;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reservation> reservations;

    public void addReservation(Reservation reservation) {
        if(this.reservations == null) {
            this.reservations = new ArrayList<>();
        }

        reservations.add(reservation);
    }
}
