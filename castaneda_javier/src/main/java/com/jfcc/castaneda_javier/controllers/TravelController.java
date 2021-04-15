package com.jfcc.castaneda_javier.controllers;

import com.jfcc.castaneda_javier.dtos.StatusCodeDTO;
import com.jfcc.castaneda_javier.dtos.hotel.BookingDTO;
import com.jfcc.castaneda_javier.dtos.hotel.BookingRequestDTO;
import com.jfcc.castaneda_javier.dtos.hotel.HotelDTO;
import com.jfcc.castaneda_javier.dtos.hotel.TicketBookingOkDTO;
import com.jfcc.castaneda_javier.exceptions.date.WrongDateFormatException;
import com.jfcc.castaneda_javier.exceptions.date.WrongDateIntervalException;
import com.jfcc.castaneda_javier.exceptions.hotel.*;
import com.jfcc.castaneda_javier.exceptions.people.WrongEmailFormatException;
import com.jfcc.castaneda_javier.services.FlightService;
import com.jfcc.castaneda_javier.services.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class TravelController {


    private HotelService hotelService;
    //private FlightService flightService;

    public TravelController(HotelService hotelService){
        this.hotelService = hotelService;
        //this.flightService = flightService;
    }

    @GetMapping("/hotels")
    public ResponseEntity<List<HotelDTO>> getHotels(@RequestParam(value = "dateFrom", required = false) String dateFrom,
                                                    @RequestParam(value = "dateTo", required = false) String dateTo,
                                                    @RequestParam(value = "destination", required = false) String destination)
            throws DestinationNotFoundException, WrongDateFormatException, WrongDateIntervalException {
        List<HotelDTO> hotels = hotelService.getHotels(dateFrom,dateTo,destination);
        return new ResponseEntity<>(hotels, HttpStatus.OK);
    }

    @PostMapping("booking")
    public TicketBookingOkDTO makeBooking(@RequestBody BookingRequestDTO bookingRequest) throws NoHotelAvailableException,
            NoHotelInDestinationAvailableException, WrongDateFormatException, WrongDateIntervalException, NoHotelInDateAvailableException, NoRoomTypeAvailableException, IOException, WrongEmailFormatException {
        return hotelService.makeBooking(bookingRequest);
    }







    @ExceptionHandler
    public ResponseEntity<StatusCodeDTO> destinationNotFoundHandler(DestinationNotFoundException exception){
        return new ResponseEntity<>(new StatusCodeDTO(400, "The destination "+exception.getMessage()+
                " doesn't exist"),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<StatusCodeDTO> wrongDateFormatFoundHandler(WrongDateFormatException exception){
        return new ResponseEntity<>(new StatusCodeDTO(400, "The date "+exception.getMessage()+
                " is mispelled or doesn't have the format dd/mm/yyy"),HttpStatus.BAD_REQUEST);
    }

}
