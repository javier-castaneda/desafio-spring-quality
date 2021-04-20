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

    public TravelController(HotelService hotelService, FlightService flightService) {
        this.hotelService = hotelService;
        this.flightService = flightService;
    }


    //Endpoint GET que retorna una lista de todos los hoteles disponibles
    //Puede usar los filtros de fecha (hoteles disponibles entre las fechas)
    //Puede usar el filtro de destino
    //En caso de no encontrar resultados retorna una lista vacía o muestra el tipo de error si hubo alguno
    @GetMapping("/hotels")
    public ResponseEntity<List<HotelDTO>> getHotels(@RequestParam(value = "dateFrom", required = false) String dateFrom,
                                                    @RequestParam(value = "dateTo", required = false) String dateTo,
                                                    @RequestParam(value = "destination", required = false) String destination)
            throws ApiException {
        List<HotelDTO> hotels = hotelService.getHotels(dateFrom, dateTo, destination);
        return new ResponseEntity<>(hotels, HttpStatus.OK);
    }

    //Endpoint POST que permite hacer una reserva en uno de los hoteles
    //En caso de haber algún error en el formato con el que se hace la reserva, se muestra un error y su código
    //Luego de hacer la reserva se cambia el estado en memoria y en el archivo (target/classes/Hoteles.csv)
    @PostMapping("/booking")
    public TicketBookingOkDTO makeBooking(@RequestBody BookingSolitudeDTO bookingSolitude) throws IOException, ApiException {
        return hotelService.makeBooking(bookingSolitude);
    }

    //Endpont GET que retorna una lista de todos los vuelos de la base de datos
    //Puede usar los filtros de fecha (vuelos disponibles entre las fechas dadas)
    //puede usar el filtro de origen
    //Puede usar el filtro de destino
    //En caso de no encontrar resultados retorna una lista vacía o se muestra el error si hubo algúno
    @GetMapping("/flights")
    public ResponseEntity<List<FlightDTO>> getFlights(@RequestParam(value = "dateFrom", required = false) String dateFrom,
                                                      @RequestParam(value = "dateTo", required = false) String dateTo,
                                                      @RequestParam(value = "origin", required = false) String origin,
                                                      @RequestParam(value = "destination", required = false) String destination)
            throws ApiException {
        List<FlightDTO> flights = flightService.getFlights(dateFrom, dateTo, origin, destination);
        return new ResponseEntity<>(flights, HttpStatus.OK);
    }

    //Endpont POST que permite hacer una reserva de asientos de un vuelo
    //En caso de haber algún error en el formato con el que se hace la reserva, se muestra un error y su código
    //No se cambia ningún estado en memoria ni en archivos
    @PostMapping("/flight-reservation")
    public TicketReservationOkDTO makeReservation(@RequestBody ReservationSolitudeDTO reservationSolitude) throws ApiException {
        return flightService.makeReservation(reservationSolitude);
    }


    //Manejo de todas las excepciones
    //Dentro de la clase ExceptionMaker se instancia un ApiException que tiene un código y un mensaje según el caso
    //Aquí se muestra el error en la respuesta
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<StatusCodeDTO> showMessage(ApiException exception) {
        switch (exception.getCode()) {
            case 400:
                return new ResponseEntity<>(new StatusCodeDTO(exception.getCode(), exception.getMessage()), HttpStatus.BAD_REQUEST);
            case 404:
                return new ResponseEntity<>(new StatusCodeDTO(exception.getCode(), exception.getMessage()), HttpStatus.NOT_FOUND);
            default:
                return new ResponseEntity<>(new StatusCodeDTO(exception.getCode(), exception.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
