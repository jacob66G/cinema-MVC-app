package com.example.Cinema.Mapper;

import com.example.Cinema.model.Dto.SeatDto;
import com.example.Cinema.model.Seat;
import org.springframework.stereotype.Component;

import java.util.List;

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
