package com.jfcc.castaneda_javier.dtos.hotel;

import com.jfcc.castaneda_javier.dtos.StatusCodeDTO;
import lombok.Data;

@Data
public class TicketBookingOkDTO {
    private String userName;
    private double amount;
    private double interest;
    private double total;
    private BookingDTO booking;
    private StatusCodeDTO statusCode;
}
