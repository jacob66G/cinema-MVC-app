package com.example.Cinema.mapper;

import com.example.Cinema.model.dto.ReservationDto;
import com.example.Cinema.model.Reservation;
import org.springframework.stereotype.Component;


@Component
public class ReservationMapper {

    public Reservation fromDto(ReservationDto dto) {
        Reservation entity = new Reservation();

        entity.setId(dto.getId());
        entity.setClientName(dto.getClientName());
        entity.setClientSurname(dto.getClientSurname());
        entity.setClientAddressEmail(dto.getClientAddressEmail());
        entity.setClientPhoneNumber(dto.getClientPhoneNumber());
        entity.setReservationDate(dto.getReservationDate());
        entity.setUser(dto.getUser());

        return entity;
    }

}
