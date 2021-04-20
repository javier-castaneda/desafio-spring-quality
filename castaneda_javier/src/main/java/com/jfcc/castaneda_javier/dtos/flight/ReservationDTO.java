package com.jfcc.castaneda_javier.dtos.flight;

import com.jfcc.castaneda_javier.dtos.general.PersonDTO;
import lombok.Data;

import java.util.List;

@Data
public class ReservationDTO {
    private String dateFrom;
    private String dateTo;
    private String origin;
    private String destination;
    private String flightNumber;
    private int seats;
    private String seatType;
    private List<PersonDTO> people;
}
