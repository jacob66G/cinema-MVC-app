package com.example.Cinema.model.Dto;

import com.example.Cinema.model.Programme;
import com.example.Cinema.model.Ticket;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ReservationDto {

    @NotBlank(message = "Imie jest wymagane")
    @Pattern(regexp = "^$|^[a-zA-Ząęćżźńłóś]+$", message = "Użyto nieprawidłowych znaków")
    private String clientName;

    @NotBlank(message = "Nazwisko jest wymagane")
    @Pattern(regexp = "^$|^[a-zA-Ząęćżźńłóś]+$", message = "Użyto nieprawidłowych znaków")
    private String clientSurname;

    @NotBlank(message = "Adres email jest wymagany")
    @Pattern(regexp = "^$|^[a-zA-Ząęćżźńłóś0-9.-@]+$", message = "Użyto nieprawidłowych znaków")
    private String clientAddressEmail;

    @NotBlank(message = "Potwierdz adres email")
    @Pattern(regexp = "^$|^[a-zA-Ząęćżźńłóś0-9.-@]+$", message = "Użyto nieprawidłowych znaków")
    private String confirmedClientAddressEmail;

    @NotBlank(message = "Numer telefonu jest wymagany")
    @Pattern(regexp = "^$|^[0-9]+$", message = "Użyto nieprawidłowych znaków")
    @Size(min=9, max = 9,message = "numer powinnien zawierać 9 cyfr")
    private String clientPhoneNumber;

    private List<Ticket> tickets;

    private LocalDateTime reservationDate;

    private Programme programme;

    private double totalPrice;

//    public static class Builder {
//        private String clientName;
//        private String clientSurname;
//        private String clientAddressEmail;
//        private String clientPhoneNumber;
//        private List<Ticket> tickets;
//        private LocalDateTime reservationDate;
//        private Programme programme;
//        private double totalPrice;
//
//        public Builder setClientName(String clientName) {
//            this.clientName = clientName;
//            return this;
//        }
//
//        public Builder setClientSurname(String clientSurname) {
//            this.clientSurname = clientSurname;
//            return this;
//        }
//
//        public Builder setClientAddressEmail(String clientAddressEmail) {
//            this.clientAddressEmail = clientAddressEmail;
//            return this;
//        }
//
//       public Builder setClientPhoneNumber(String clientPhoneNumber) {
//            this.clientPhoneNumber = clientPhoneNumber;
//            return this;
//       }
//
//       public Builder setTickets(List<Ticket> tickets) {
//            this.tickets = tickets;
//            return this;
//       }
//
//       public Builder setReservationDate(LocalDateTime reservationDate) {
//            this.reservationDate = reservationDate;
//            return this;
//       }
//
//       public Builder setProgramme(Programme programme) {
//            this.programme = programme;
//            return this;
//       }
//
//       public Builder setTotalPrice(double totalPrice) {
//            this.totalPrice = totalPrice;
//            return this;
//       }
//
//       public ReservationDto build() {
//           ReservationDto reservationDto = new ReservationDto();
//           reservationDto.clientName = this.clientName;
//           reservationDto.clientSurname = this.clientSurname;
//           reservationDto.clientAddressEmail = this.clientAddressEmail;
//           reservationDto.clientPhoneNumber = this.clientPhoneNumber;
//           reservationDto.tickets = this.tickets;
//           reservationDto.reservationDate = this.reservationDate;
//           reservationDto.programme = this.programme;
//           reservationDto.totalPrice = this.totalPrice;
//           return reservationDto;
//       }
//    }
}
