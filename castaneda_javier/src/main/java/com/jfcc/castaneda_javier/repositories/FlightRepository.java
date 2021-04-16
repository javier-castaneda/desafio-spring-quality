package com.jfcc.castaneda_javier.repositories;

import com.jfcc.castaneda_javier.dtos.flight.FlightDTO;
import com.jfcc.castaneda_javier.dtos.hotel.HotelDTO;

import java.util.List;

public interface FlightRepository {

    public List<FlightDTO> getAllFlights();
}
