package com.example.Cinema.mapper;

import com.example.Cinema.model.dto.SeatDto;
import com.example.Cinema.model.Seat;
import org.springframework.stereotype.Component;

@Component
public class SeatMapper {

    public SeatDto toDto(Seat seat, boolean isBooked) {
        return new SeatDto(
                seat.getIdseat(),
                seat.getRow(),
                seat.getNumber(),
                isBooked,
                false
        );
    }
}
