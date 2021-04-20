package com.jfcc.castaneda_javier.services;

import com.jfcc.castaneda_javier.dtos.StatusCodeDTO;
import com.jfcc.castaneda_javier.dtos.general.PersonDTO;
import com.jfcc.castaneda_javier.dtos.hotel.*;
import com.jfcc.castaneda_javier.exceptions.ApiException;
import com.jfcc.castaneda_javier.exceptions.ExceptionMaker;
import com.jfcc.castaneda_javier.repositories.HotelRepository;
import com.jfcc.castaneda_javier.utils.DateUtils;
import com.jfcc.castaneda_javier.utils.VerificationUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HotelServiceImpl implements HotelService {


    private HotelRepository hotelRepository;

    public HotelServiceImpl(HotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
    }

    @Override
    public List<HotelDTO> getHotels(String dateFrom, String dateTo, String destination) throws ApiException {

        List<HotelDTO> hotels = hotelRepository.getAllHotels();

        hotels = hotels.stream().filter(hotelDTO -> hotelDTO.getBooked().equals(false)).collect(Collectors.toList());


        if (destination != null) {
            hotels = hotels.stream().filter(hotelDTO -> hotelDTO.getCity().equals(destination))
                    .collect(Collectors.toList());
            if (hotels.size() == 0) {
                throw ExceptionMaker.getException("DEST1");
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
                hotels = hotels.stream().filter(hotelDTO -> DateUtils.isBetween(localDateFrom, hotelDTO.getAvailableFrom(), hotelDTO.getAvailableTo())
                        && DateUtils.isBetween(localDateTo, hotelDTO.getAvailableFrom(), hotelDTO.getAvailableTo()))
                        .collect(Collectors.toList());
            }
        }
        return hotels;
    }

    @Override
    public TicketBookingOkDTO makeBooking(BookingSolitudeDTO bookingSolitude) throws IOException, ApiException {

        BookingRequestDTO bookingRequest = bookingSolitude.getBooking();

        //Verificar datos:
        List<HotelDTO> hotels = hotelRepository.getAllHotels();

        VerificationUtils.checkEmail(bookingSolitude.getUserName());

        hotels =hotels.stream().filter(hotelDTO -> hotelDTO.getCode().equals(bookingRequest.getHotelCode())).collect(Collectors.toList());
        if (hotels.size() < 1) {
            throw ExceptionMaker.getException("NOHOT4");
        }

        //filtrar disponibilidad
        hotels = hotels.stream().filter(hotelDTO -> hotelDTO.getBooked().equals(false)).collect(Collectors.toList());
        if (hotels.size() < 1) {
            throw ExceptionMaker.getException("NOHOT1");
        }
        //Verificar destino
        hotels = hotels.stream().filter(hotelDTO -> hotelDTO.getCity().equals(bookingRequest.getDestination())).collect(Collectors.toList());
        if (hotels.size() < 1) {
            throw ExceptionMaker.getException("DEST1");
        }
        //verificar fechas
        LocalDate dateFrom = DateUtils.makeLocalDate(bookingRequest.getDateFrom());
        LocalDate dateTo = DateUtils.makeLocalDate(bookingRequest.getDateTo());
        if (dateTo.isBefore(dateFrom)) {
            throw ExceptionMaker.getException("DATE2");
        }
        hotels = hotels.stream().filter(hotelDTO -> DateUtils.isBetween(dateFrom, hotelDTO.getAvailableFrom(), hotelDTO.getAvailableTo()) &&
                DateUtils.isBetween(dateTo, hotelDTO.getAvailableFrom(), hotelDTO.getAvailableTo())).collect(Collectors.toList());
        if (hotels.size() < 1) {
            throw ExceptionMaker.getException("NOHOT2");
        }
        //verificar tipo de habitación y cantidad de personas


        switch (bookingRequest.getRoomType()) {
            case "Single":
                if (bookingRequest.getPeopleAmount() > 1 || bookingRequest.getPeople().size() > 1) {
                    throw ExceptionMaker.getException("PPL1");
                }
                break;

            case "Doble":
                if (bookingRequest.getPeopleAmount() > 2 || bookingRequest.getPeople().size() > 2) {
                    throw ExceptionMaker.getException("PPL1");
                }
                break;

            case "Triple":
                if (bookingRequest.getPeopleAmount() > 3 || bookingRequest.getPeople().size() > 3) {
                    throw ExceptionMaker.getException("PPL1");
                }
                break;

            case "Múltiple":
                if (bookingRequest.getPeopleAmount() > 10 || bookingRequest.getPeople().size() > 10 ||
                        bookingRequest.getPeopleAmount() < 4 || bookingRequest.getPeople().size() < 4) {
                    throw ExceptionMaker.getException("PPL1");
                }
                break;

            default:
                throw ExceptionMaker.getException("ROOM1");
        }

        hotels = hotels.stream().filter(hotelDTO -> hotelDTO.getRoomType().equals(bookingRequest.getRoomType())).collect(Collectors.toList());
        if (hotels.size() < 1) {
            throw ExceptionMaker.getException("ROOM1");
        }


        //verificar datos de personas
        for (PersonDTO person : bookingRequest.getPeople()) {
            VerificationUtils.checkEmail(person.getMail());
        }

        HotelDTO actual = hotels.get(0);


        TicketBookingOkDTO ticket = new TicketBookingOkDTO();

        ticket.setUserName(bookingSolitude.getUserName());
        int numberOfDays = (int) ChronoUnit.DAYS.between(dateFrom, dateTo)-1;
        ticket.setAmount(actual.getPriceByNight()*numberOfDays);
        boolean payingDebit = false;
        //verificar el interés

        //verificar datos de pago
        switch (bookingRequest.getPaymentMethod().getType()) {
            case "CREDIT":
                //Preguntar sobre el intervalo de interés
                ticket.setInterest(((bookingRequest.getPaymentMethod().getDues() / 3) + 1) * 5);
                ticket.setTotal(ticket.getAmount() * (1 + (ticket.getInterest() / 100)));
                break;
            case "DEBIT":
                ticket.setInterest(0);
                ticket.setTotal(ticket.getAmount());
                if (bookingRequest.getPaymentMethod().getDues() > 1)
                    payingDebit = true;
                break;
        }

        ticket.setBooking(toBookingDTO(bookingRequest));

        //cambiar estado en lista
        //actualizar repositorio
        hotelRepository.changeState(bookingRequest.getHotelCode());


        ticket.setStatusCode(new StatusCodeDTO(200, "El proceso terminó satisfactoriamente." +
                (payingDebit ? " Se realiza el pago en " + bookingRequest.getPaymentMethod().getDues() + " cuotas" : "")));

        //retornar el ticket
        return ticket;
    }




    private BookingDTO toBookingDTO(BookingRequestDTO bookingRequest){
        BookingDTO bookingForTicket = new BookingDTO();
        bookingForTicket.setDateFrom(bookingRequest.getDateFrom());
        bookingForTicket.setDateTo(bookingRequest.getDateTo());
        bookingForTicket.setDestination(bookingRequest.getDestination());
        bookingForTicket.setHotelCode(bookingRequest.getHotelCode());
        bookingForTicket.setPeopleAmount(bookingRequest.getPeopleAmount());
        bookingForTicket.setRoomType(bookingRequest.getRoomType());
        bookingForTicket.setPeople(bookingRequest.getPeople());
        return bookingForTicket;
    }
}
