package com.jfcc.castaneda_javier.dtos.flight;

import com.jfcc.castaneda_javier.dtos.StatusCodeDTO;
import lombok.Data;

@Data
public class TicketReservationOkDTO {
    private String userName;
    private double amount;
    private double interest;
    private double total;

    private ReservationDTO flightReservation;
    private StatusCodeDTO statusCode;

}
