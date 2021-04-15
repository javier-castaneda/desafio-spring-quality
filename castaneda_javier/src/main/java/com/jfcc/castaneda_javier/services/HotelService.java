package com.jfcc.castaneda_javier.services;

import com.jfcc.castaneda_javier.dtos.hotel.BookingRequestDTO;
import com.jfcc.castaneda_javier.dtos.hotel.HotelDTO;
import com.jfcc.castaneda_javier.dtos.hotel.TicketBookingOkDTO;
import com.jfcc.castaneda_javier.exceptions.date.WrongDateFormatException;
import com.jfcc.castaneda_javier.exceptions.date.WrongDateIntervalException;
import com.jfcc.castaneda_javier.exceptions.hotel.*;
import com.jfcc.castaneda_javier.exceptions.people.WrongEmailFormatException;

import java.io.IOException;
import java.util.List;

public interface HotelService {

    List<HotelDTO> getHotels(String dateFrom, String dateTo, String destination) throws DestinationNotFoundException, WrongDateFormatException, WrongDateIntervalException;
    TicketBookingOkDTO makeBooking(BookingRequestDTO bookingRequest) throws NoHotelAvailableException, NoHotelInDestinationAvailableException, WrongDateFormatException, WrongDateIntervalException, NoHotelInDateAvailableException, NoRoomTypeAvailableException, WrongEmailFormatException, IOException;
}
