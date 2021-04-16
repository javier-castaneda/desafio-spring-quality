package com.jfcc.castaneda_javier.services;

import com.jfcc.castaneda_javier.dtos.flight.FlightDTO;
import com.jfcc.castaneda_javier.dtos.hotel.HotelDTO;
import com.jfcc.castaneda_javier.exceptions.ApiException;
import com.jfcc.castaneda_javier.exceptions.ExceptionMaker;
import com.jfcc.castaneda_javier.repositories.FlightRepository;
import com.jfcc.castaneda_javier.repositories.FlightRepositoryImpl;
import com.jfcc.castaneda_javier.utils.DateUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FlightServiceImpl implements FlightService{

    private FlightRepository flightRepository;

    public FlightServiceImpl(FlightRepository flightRepository){
        this.flightRepository = flightRepository;
    }

    @Override
    public List<FlightDTO> getFlights(String dateFrom, String dateTo, String origin, String destination) throws ApiException {
        List<FlightDTO> flights = flightRepository.getAllFlights();

        if (destination != null) {
            flights = flights.stream().filter(flightDTO -> flightDTO.getDestination().equals(destination))
                    .collect(Collectors.toList());
            if (flights.size() == 0) {
                throw ExceptionMaker.getException("DEST1");
            }
        }

        if (origin != null) {
            flights = flights.stream().filter(flightDTO -> flightDTO.getOrigin().equals(origin))
                    .collect(Collectors.toList());
            if (flights.size() == 0) {
                throw ExceptionMaker.getException("ORIG1");
            }
        }

        if (dateFrom != null && dateTo != null) {
            //Parseo de fechas
            LocalDate localDateFrom = DateUtils.makeLocalDate(dateFrom);
            LocalDate localDateTo = DateUtils.makeLocalDate(dateTo);
            //Verificación de validéz
            if (localDateTo.isBefore(localDateFrom)) {
                throw ExceptionMaker.getException("DATE2");
            }
            if (localDateTo != null && localDateTo != null) {
                flights = flights.stream().filter(flightDTO -> DateUtils.isBetween(localDateFrom, flightDTO.getDateFrom(), flightDTO.getDateTo())
                        && DateUtils.isBetween(localDateTo, flightDTO.getDateFrom(), flightDTO.getDateTo()))
                        .collect(Collectors.toList());
            }
        }
        return flights;

    }
}
