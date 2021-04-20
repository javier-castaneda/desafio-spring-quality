package com.jfcc.castaneda_javier.services;

import com.jfcc.castaneda_javier.dtos.flight.FlightDTO;
import com.jfcc.castaneda_javier.dtos.flight.ReservationSolitudeDTO;
import com.jfcc.castaneda_javier.dtos.flight.TicketReservationOkDTO;
import com.jfcc.castaneda_javier.dtos.hotel.BookingSolitudeDTO;
import com.jfcc.castaneda_javier.dtos.hotel.HotelDTO;
import com.jfcc.castaneda_javier.dtos.hotel.TicketBookingOkDTO;
import com.jfcc.castaneda_javier.exceptions.ApiException;

import java.util.List;

public interface FlightService {
    List<FlightDTO> getFlights(String dateFrom, String dateTo, String origin, String destination) throws ApiException;
    TicketReservationOkDTO makeReservation(ReservationSolitudeDTO solitude) throws ApiException;
}
