package com.jfcc.castaneda_javier.dtos.hotel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HotelDTO {
    private String code;
    private String name;
    private String city;
    private String roomType;
    private Long priceByNight;
    private LocalDate availableFrom;
    private LocalDate availableTo;
    private Boolean booked;
}
