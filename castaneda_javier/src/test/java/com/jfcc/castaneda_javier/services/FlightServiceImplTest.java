package com.jfcc.castaneda_javier.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jfcc.castaneda_javier.dtos.flight.FlightDTO;
import com.jfcc.castaneda_javier.dtos.flight.ReservationSolitudeDTO;
import com.jfcc.castaneda_javier.dtos.flight.TicketReservationOkDTO;
import com.jfcc.castaneda_javier.exceptions.ApiException;
import com.jfcc.castaneda_javier.repositories.FlightRepository;
import com.jfcc.castaneda_javier.repositories.FlightRepositoryImpl;
import com.jfcc.castaneda_javier.utils.DateUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FlightServiceImplTest {

    private FlightRepository flightRepository;
    private FlightService flightService;

    @BeforeEach
    void setUp() {
        flightRepository = mock(FlightRepositoryImpl.class);
        flightService = new FlightServiceImpl(flightRepository);
    }

    @Test
    void ShouldGetFlights() throws ApiException {
        when(flightRepository.getAllFlights()).thenReturn(getList());

        List<FlightDTO> result = flightService.getFlights(null, null, null, null);

        assertThat(result).isEqualTo(getList());
    }

    @Test
    void shouldGetFilteredFlights() throws ApiException {
        when(flightRepository.getAllFlights()).thenReturn(getList());

        List<FlightDTO> result = flightService.getFlights(null, null, "Bogotá", "Buenos Aires");

        verify(flightRepository,atLeastOnce()).getAllFlights();

        assertThat(result).isEqualTo(getList().stream().filter(flightDTO -> flightDTO.getOrigin().equals("Bogotá") &&
                flightDTO.getDestination().equals("Buenos Aires")).collect(Collectors.toList()));
    }

    @Test
    void shouldGetFilteredByDate() throws ApiException {
        when(flightRepository.getAllFlights()).thenReturn(getList());

        List<FlightDTO> result = flightService.getFlights("10/02/2021","13/02/2021",null,null);

        verify(flightRepository,atLeastOnce()).getAllFlights();

        assertThat(result).isEqualTo(getList().stream().filter(flightDTO -> DateUtils.isBetween(LocalDate.of(2021,02,10), flightDTO.getDateFrom(), flightDTO.getDateTo())
                && DateUtils.isBetween(LocalDate.of(2021,02,13), flightDTO.getDateFrom(), flightDTO.getDateTo()))
                .collect(Collectors.toList()));
    }

    @Test
    void makeReservation() throws ApiException {
        when(flightRepository.getAllFlights()).thenReturn(getList());

        ReservationSolitudeDTO solitude = loadReservationFromFile("reservationRequest.json");
        TicketReservationOkDTO response = loadReservationTicketFromFile("reservationResult.json");

        assertThat(response).isEqualTo(flightService.makeReservation(solitude));

    }

    @Test
    void makeReservationCredit() throws ApiException {
        when(flightRepository.getAllFlights()).thenReturn(getList());

        ReservationSolitudeDTO solitude = loadReservationFromFile("reservationRequestCredit.json");
        TicketReservationOkDTO response = loadReservationTicketFromFile("reservationResultCredit.json");

        assertThat(response).isEqualTo(flightService.makeReservation(solitude));

    }

    private List<FlightDTO> getList() {

        FlightDTO flight1 = new FlightDTO("BAPI-1235", "Buenos Aires", "Puerto Iguazú", "Economy", (long) 6500, LocalDate.of(2021, 02, 10), LocalDate.of(2021, 02, 15));
        FlightDTO flight2 = new FlightDTO("PIBA-1420", "Puerto Iguazú", "Bogotá", "Business", (long) 43200, LocalDate.of(2021, 02, 10), LocalDate.of(2021, 02, 20));
        FlightDTO flight3 = new FlightDTO("PIBA-1420", "Puerto Iguazú", "Bogotá", "Economy", (long) 25735, LocalDate.of(2021, 02, 10), LocalDate.of(2021, 02, 21));
        FlightDTO flight4 = new FlightDTO("BATU-5536", "Buenos Aires", "Tucumán", "Economy", (long) 7320, LocalDate.of(2021, 02, 10), LocalDate.of(2021, 02, 17));
        FlightDTO flight5 = new FlightDTO("TUPI-3369", "Tucumán", "Puerto Iguazú", "Business", (long) 12530, LocalDate.of(2021, 02, 12), LocalDate.of(2021, 02, 23));
        FlightDTO flight6 = new FlightDTO("TUPI-3369", "Tucumán", "Puerto Iguazú", "Economy", (long) 5400, LocalDate.of(2021, 01, 02), LocalDate.of(2021, 01, 16));
        FlightDTO flight7 = new FlightDTO("BOCA-4213", "Bogotá", "Cartagena", "Economy", (long) 8000, LocalDate.of(2021, 01, 02), LocalDate.of(2021, 01, 16));
        FlightDTO flight8 = new FlightDTO("CAME-0321", "Cartagena", "Medellín", "Economy", (long) 7800, LocalDate.of(2021, 01, 23), LocalDate.of(2021, 01, 31));
        FlightDTO flight9 = new FlightDTO("BOBA-6567", "Bogotá", "Buenos Aires", "Business", (long) 57000, LocalDate.of(2021, 02, 15), LocalDate.of(2021, 02, 28));
        FlightDTO flight10 = new FlightDTO("BOBA-6567", "Bogotá", "Buenos Aires", "Economy", (long) 39860, LocalDate.of(2021, 03, 01), LocalDate.of(2021, 03, 14));
        FlightDTO flight11 = new FlightDTO("BOME-4442", "Bogotá", "Medellín", "Economy", (long) 11000, LocalDate.of(2021, 02, 10), LocalDate.of(2021, 02, 24));
        FlightDTO flight12 = new FlightDTO("MEPI-9986", "Medellín", "Puerto Iguazú", "Business", (long) 41460, LocalDate.of(2021, 04, 17), LocalDate.of(2021, 05, 02));

        return List.of(flight1, flight2, flight3, flight4, flight5, flight6, flight7, flight8, flight9, flight10, flight11, flight12);

    }

    private ReservationSolitudeDTO loadReservationFromFile(String fileName) {
        File file = null;

        try {
            file = ResourceUtils.getFile("classpath:" + fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ObjectMapper objectMapper = new ObjectMapper();
        TypeReference<ReservationSolitudeDTO> typeReference = new TypeReference<>() {
        };
        ReservationSolitudeDTO reservationRequest = null;
        try {
            reservationRequest = objectMapper.readValue(file, typeReference);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return reservationRequest;
    }

    private TicketReservationOkDTO loadReservationTicketFromFile(String fileName) {
        File file = null;

        try {
            file = ResourceUtils.getFile("classpath:" + fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ObjectMapper objectMapper = new ObjectMapper();
        TypeReference<TicketReservationOkDTO> typeReference = new TypeReference<>() {
        };
        TicketReservationOkDTO ticket = null;
        try {
            ticket = objectMapper.readValue(file, typeReference);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ticket;
    }
}

