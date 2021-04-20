package com.jfcc.castaneda_javier.services;

import com.jfcc.castaneda_javier.dtos.StatusCodeDTO;
import com.jfcc.castaneda_javier.dtos.flight.*;
import com.jfcc.castaneda_javier.dtos.general.PersonDTO;
import com.jfcc.castaneda_javier.exceptions.ApiException;
import com.jfcc.castaneda_javier.exceptions.ExceptionMaker;
import com.jfcc.castaneda_javier.repositories.FlightRepository;
import com.jfcc.castaneda_javier.utils.DateUtils;
import com.jfcc.castaneda_javier.utils.VerificationUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FlightServiceImpl implements FlightService {

    private FlightRepository flightRepository;

    public FlightServiceImpl(FlightRepository flightRepository) {
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

    public TicketReservationOkDTO makeReservation(ReservationSolitudeDTO solitude) throws ApiException {

        ReservationRequestDTO reservationRequest = solitude.getFlightReservation();

        List<FlightDTO> flights = flightRepository.getAllFlights();

        VerificationUtils.checkEmail(solitude.getUserName());

        //Verificar Código de vuelo
        flights = flights.stream().filter(flightDTO -> flightDTO.getFlightNumber().equals(reservationRequest.getFlightNumber())).collect(Collectors.toList());
        System.out.println("Código de vuelo de reserva: " + reservationRequest.getFlightNumber());
        if (flights.size() < 1) {
            throw ExceptionMaker.getException("NOFLY1");
        }

        //verificar tipo de asientos
        flights = flights.stream().filter(flightDTO -> flightDTO.getSeatType().equals(reservationRequest.getSeatType())).collect(Collectors.toList());
        if (flights.size() < 1) {
            throw ExceptionMaker.getException("NOFLY4");
        }

        //Verificar destino
        flights = flights.stream().filter(flightDTO -> flightDTO.getOrigin().equals(reservationRequest.getOrigin())).collect(Collectors.toList());
        if (flights.size() < 1) {
            throw ExceptionMaker.getException("NOFLY2");
        }

        //Verificar destino
        flights = flights.stream().filter(flightDTO -> flightDTO.getDestination().equals(reservationRequest.getDestination())).collect(Collectors.toList());
        if (flights.size() < 1) {
            throw ExceptionMaker.getException("NOFLY3");
        }

        //Verificar fechas
        LocalDate dateFrom = DateUtils.makeLocalDate(reservationRequest.getDateFrom());
        LocalDate dateTo = DateUtils.makeLocalDate(reservationRequest.getDateTo());

        if (dateTo.isBefore(dateFrom)) {
            throw ExceptionMaker.getException("DATE2");
        }

        if (!flights.get(0).getDateFrom().isEqual(dateFrom) || !flights.get(0).getDateTo().isEqual(dateTo)) {
            throw ExceptionMaker.getException("DATE3");
        }


        //verificar cantidad de asientos
        if (reservationRequest.getPeople().size() != reservationRequest.getSeats()) {
            throw ExceptionMaker.getException("SEAT1");
        }

        //Verficar datos de personas
        for (PersonDTO person : reservationRequest.getPeople()) {
            VerificationUtils.checkEmail(person.getMail());
        }

        FlightDTO actual = flights.get(0);

        TicketReservationOkDTO ticket = new TicketReservationOkDTO();

        ticket.setUserName(solitude.getUserName());
        ticket.setAmount(actual.getPriceByPerson() * reservationRequest.getSeats());
        boolean payingDebit = false;
        //verificar el interés

        //verificar datos de pago
        switch (reservationRequest.getPaymentMethod().getType()) {
            case "CREDIT":
                //Preguntar sobre el intervalo de interés
                ticket.setInterest(((reservationRequest.getPaymentMethod().getDues() / 3) + 1) * 5);
                ticket.setTotal(ticket.getAmount() * (1 + (ticket.getInterest() / 100)));
                break;
            case "DEBIT":
                ticket.setInterest(0);
                ticket.setTotal(ticket.getAmount());
                if (reservationRequest.getPaymentMethod().getDues() > 1)
                    payingDebit = true;
                break;
        }

        ticket.setFlightReservation(toReservationDTO(reservationRequest));

        //actualizar repositorio

        ticket.setStatusCode(new StatusCodeDTO(200, "El proceso terminó satisfactoriamente." +
                (payingDebit ? " Se realiza el pago en " + reservationRequest.getPaymentMethod().getDues() + " cuotas" : "")));

        return ticket;
    }

    private ReservationDTO toReservationDTO(ReservationRequestDTO reservationRequest) {
        ReservationDTO reservationForTicket = new ReservationDTO();
        reservationForTicket.setDateFrom(reservationRequest.getDateFrom());
        reservationForTicket.setDateTo(reservationRequest.getDateTo());
        reservationForTicket.setOrigin(reservationRequest.getOrigin());
        reservationForTicket.setDestination(reservationRequest.getDestination());
        reservationForTicket.setFlightNumber(reservationRequest.getFlightNumber());
        reservationForTicket.setSeats(reservationRequest.getSeats());
        reservationForTicket.setSeatType(reservationRequest.getSeatType());
        reservationForTicket.setPeople(reservationRequest.getPeople());
        return reservationForTicket;
    }

}
