package com.jfcc.castaneda_javier.services;

import com.jfcc.castaneda_javier.dtos.hotel.BookingSolitudeDTO;
import com.jfcc.castaneda_javier.dtos.hotel.HotelDTO;
import com.jfcc.castaneda_javier.dtos.hotel.TicketBookingOkDTO;
import com.jfcc.castaneda_javier.exceptions.ApiException;

import java.io.IOException;
import java.util.List;

public interface HotelService {

    List<HotelDTO> getHotels(String dateFrom, String dateTo, String destination) throws ApiException;
    TicketBookingOkDTO makeBooking(BookingSolitudeDTO bookingSolitude) throws IOException, ApiException;
}
