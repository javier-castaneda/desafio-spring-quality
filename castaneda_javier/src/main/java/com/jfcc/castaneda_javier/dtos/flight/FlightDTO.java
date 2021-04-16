package com.jfcc.castaneda_javier.dtos.flight;

import lombok.Data;

import java.time.LocalDate;

@Data
public class FlightDTO {
    private String flightNumber;
    private String origin;
    private String destination;
    private String seatType;
    private Long priceByPerson;
    private LocalDate dateFrom;
    private LocalDate dateTo;
}
