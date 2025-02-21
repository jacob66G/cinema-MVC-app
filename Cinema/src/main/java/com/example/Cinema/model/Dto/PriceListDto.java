package com.example.Cinema.model.Dto;

import jakarta.validation.Valid;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PriceListDto {

    @Valid
    private List<PriceDto> priceList = new ArrayList<>();
}
