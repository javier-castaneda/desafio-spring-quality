package com.jfcc.castaneda_javier.dtos.hotel;

import com.jfcc.castaneda_javier.dtos.general.PersonDTO;
import lombok.Data;

import java.util.List;

@Data
public class BookingDTO {
    private String userName;
    private String dateFrom;
    private String dateTo;
    private String destination;
    private String hotelCode;
    private int peopleAmount;
    private String roomType;
    private List<PersonDTO> people;
}
