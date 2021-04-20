package com.jfcc.castaneda_javier.controllers;

import com.jfcc.castaneda_javier.dtos.StatusCodeDTO;
import com.jfcc.castaneda_javier.dtos.flight.FlightDTO;
import com.jfcc.castaneda_javier.dtos.flight.ReservationSolitudeDTO;
import com.jfcc.castaneda_javier.dtos.flight.TicketReservationOkDTO;
import com.jfcc.castaneda_javier.dtos.hotel.*;
import com.jfcc.castaneda_javier.exceptions.ApiException;
import com.jfcc.castaneda_javier.services.FlightService;
import com.jfcc.castaneda_javier.services.HotelService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class TravelController {


    private HotelService hotelService;
    private FlightService flightService;

    public TravelController(HotelService hotelService, FlightService flightService){
        this.hotelService = hotelService;
        this.flightService = flightService;
    }

    @GetMapping("/hotels")
    public ResponseEntity<List<HotelDTO>> getHotels(@RequestParam(value = "dateFrom", required = false) String dateFrom,
                                                    @RequestParam(value = "dateTo", required = false) String dateTo,
                                                    @RequestParam(value = "destination", required = false) String destination)
            throws ApiException {
        List<HotelDTO> hotels = hotelService.getHotels(dateFrom,dateTo,destination);
        return new ResponseEntity<>(hotels, HttpStatus.OK);
    }

    @PostMapping("/booking")
    public TicketBookingOkDTO makeBooking(@RequestBody BookingSolitudeDTO bookingSolitude) throws IOException, ApiException {
        return hotelService.makeBooking(bookingSolitude);
    }

    @GetMapping("/flights")
    public ResponseEntity<List<FlightDTO>> getFlights(@RequestParam(value = "dateFrom", required = false) String dateFrom,
                                                      @RequestParam(value = "dateTo", required = false) String dateTo,
                                                      @RequestParam(value = "origin", required = false) String origin,
                                                      @RequestParam(value = "destination", required = false) String destination)
            throws ApiException {
        List<FlightDTO> flights = flightService.getFlights(dateFrom, dateTo,origin,destination);
        return new ResponseEntity<>(flights, HttpStatus.OK);
    }

    @PostMapping("/flight-reservation")
    public TicketReservationOkDTO makeReservation(@RequestBody ReservationSolitudeDTO reservationSolitude) throws ApiException {
        return flightService.makeReservation(reservationSolitude);
    }


    @ExceptionHandler(ApiException.class)
    public ResponseEntity<StatusCodeDTO> showMessage(ApiException exception){
        switch (exception.getCode()){
            case 400:
                return new ResponseEntity<>(new StatusCodeDTO(exception.getCode(),exception.getMessage()),HttpStatus.BAD_REQUEST);
            case 404:
                return new ResponseEntity<>(new StatusCodeDTO(exception.getCode(),exception.getMessage()),HttpStatus.NOT_FOUND);
            default:
                return new ResponseEntity<>(new StatusCodeDTO(exception.getCode(),exception.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
