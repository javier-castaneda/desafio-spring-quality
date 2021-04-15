package com.jfcc.castaneda_javier.services;

import com.jfcc.castaneda_javier.dtos.StatusCodeDTO;
import com.jfcc.castaneda_javier.dtos.general.PersonDTO;
import com.jfcc.castaneda_javier.dtos.hotel.BookingDTO;
import com.jfcc.castaneda_javier.dtos.hotel.BookingRequestDTO;
import com.jfcc.castaneda_javier.dtos.hotel.HotelDTO;
import com.jfcc.castaneda_javier.dtos.hotel.TicketBookingOkDTO;
import com.jfcc.castaneda_javier.exceptions.date.WrongDateFormatException;
import com.jfcc.castaneda_javier.exceptions.date.WrongDateIntervalException;
import com.jfcc.castaneda_javier.exceptions.hotel.*;
import com.jfcc.castaneda_javier.exceptions.people.WrongEmailFormatException;
import com.jfcc.castaneda_javier.repositories.HotelRepository;
import com.jfcc.castaneda_javier.utils.DateUtils;
import org.apache.commons.validator.EmailValidator;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HotelServiceImpl implements HotelService {


    private HotelRepository hotelRepository;

    public HotelServiceImpl(HotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
    }

    @Override
    public List<HotelDTO> getHotels(String dateFrom, String dateTo, String destination) throws DestinationNotFoundException, WrongDateFormatException, WrongDateIntervalException {

        List<HotelDTO> hotels = hotelRepository.getAllHotels();
        //Parseo de fechas

        if (destination != null) {
            hotels = hotels.stream().filter(hotelDTO -> hotelDTO.getCity().equals(destination))
                    .collect(Collectors.toList());
            if (hotels.size() == 0) {
                throw new DestinationNotFoundException(destination);
            }
        }

        if (dateFrom != null && dateTo != null) {
            LocalDate localDateFrom = DateUtils.makeLocalDate(dateFrom);
            LocalDate localDateTo = DateUtils.makeLocalDate(dateTo);
            //Verificación de validéz
            if (localDateTo.isBefore(localDateFrom)) {
                throw new WrongDateIntervalException();
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
    public TicketBookingOkDTO makeBooking(BookingRequestDTO bookingRequest) throws NoHotelAvailableException, NoHotelInDestinationAvailableException, WrongDateFormatException, WrongDateIntervalException, NoHotelInDateAvailableException, NoRoomTypeAvailableException, WrongEmailFormatException, IOException {


        //Verificar datos:
        List<HotelDTO> hotels = hotelRepository.getAllHotels();
        //filtrar disponibilidad
        hotels = hotels.stream().filter(hotelDTO -> hotelDTO.getBooked().equals(false)).collect(Collectors.toList());
        if (hotels.size() < 1) {
            throw new NoHotelAvailableException();
        }
        //Verificar destino
        hotels.stream().filter(hotelDTO -> hotelDTO.getCity().equals(bookingRequest.getDestination())).collect(Collectors.toList());
        if (hotels.size() < 1) {
            throw new NoHotelInDestinationAvailableException(bookingRequest.getDestination());
        }
        //verificar fechas
        LocalDate dateFrom = DateUtils.makeLocalDate(bookingRequest.getDateFrom());
        LocalDate dateTo = DateUtils.makeLocalDate(bookingRequest.getDateTo());
        if (dateTo.isBefore(dateFrom)) {
            throw new WrongDateIntervalException();
        }
        hotels.stream().filter(hotelDTO -> DateUtils.isBetween(dateFrom, hotelDTO.getAvailableFrom(), hotelDTO.getAvailableTo()) &&
                DateUtils.isBetween(dateTo, hotelDTO.getAvailableFrom(), hotelDTO.getAvailableTo()));
        if (hotels.size() < 1) {
            throw new NoHotelInDateAvailableException();
        }
        //verificar tipo de habitación y cantidad de personas


        switch (bookingRequest.getRoomType()) {
            case "Single":
                if (bookingRequest.getPeopleAmount() > 1 || bookingRequest.getPeople().size() > 1) {
                    //throw WrongAmountExeption
                }
                break;

            case "Doble":
                if (bookingRequest.getPeopleAmount() > 2 || bookingRequest.getPeople().size() > 2) {
                    //throw WrongAmountExeption
                }
                break;

            case "Triple":
                if (bookingRequest.getPeopleAmount() > 3 || bookingRequest.getPeople().size() > 3) {
                    //throw WrongAmountExeption
                }
                break;

            case "Múltiple":
                if (bookingRequest.getPeopleAmount() > 10 || bookingRequest.getPeople().size() > 10 ||
                        bookingRequest.getPeopleAmount() < 4 || bookingRequest.getPeople().size() < 4) {
                    //throw WrongAmountExeption
                }
                break;

            default:
                //throw BadRoomTypeException
        }

        hotels.stream().filter(hotelDTO -> hotelDTO.getRoomType().equals(bookingRequest.getRoomType())).collect(Collectors.toList());
        if (hotels.size() < 1) {
            throw new NoRoomTypeAvailableException();
        }

        hotels.stream().filter(hotelDTO -> hotelDTO.getCode().equals(bookingRequest.getHotelCode())).collect(Collectors.toList());
        if (hotels.size() != 1) {
            throw new NoRoomTypeAvailableException();
        }

        //verificar datos de personas
        for (PersonDTO person : bookingRequest.getPeople()) {
            checkEmail(person.getMail());
        }

        HotelDTO actual = hotels.get(0);

        //crear el Ticket
        TicketBookingOkDTO ticket = new TicketBookingOkDTO();
        BookingDTO bookingForTicket = new BookingDTO();
        bookingForTicket.setDateFrom(bookingRequest.getDateFrom());
        bookingForTicket.setDateTo(bookingRequest.getDateTo());
        bookingForTicket.setDestination(bookingRequest.getDestination());
        bookingForTicket.setHotelCode(bookingRequest.getHotelCode());
        bookingForTicket.setPeopleAmount(bookingRequest.getPeopleAmount());
        bookingForTicket.setRoomType(bookingRequest.getRoomType());
        bookingForTicket.setPeople(bookingRequest.getPeople());
        ticket.setUserName(bookingRequest.getUserName());
        ticket.setAmount(actual.getPriceByNight());
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

        ticket.setBooking(bookingForTicket);

        //cambiar estado en lista
        //actualizar repositorio
        hotelRepository.changeState(bookingRequest.getHotelCode());


        ticket.setStatusCode(new StatusCodeDTO(200, "El proceso terminó satisfactoriamente. " +
                (payingDebit ? "Se realiza el pago en " + bookingRequest.getPaymentMethod().getDues() + " cuotas" : "")));

        //retornar el ticket
        return ticket;
    }


    private void checkEmail(String email) throws WrongEmailFormatException {
        EmailValidator validator = EmailValidator.getInstance();
        if (!validator.isValid(email)) {
            throw new WrongEmailFormatException();
        }
    }
}
