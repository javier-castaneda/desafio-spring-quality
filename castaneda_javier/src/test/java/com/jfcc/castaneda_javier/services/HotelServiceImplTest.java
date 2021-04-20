package com.jfcc.castaneda_javier.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jfcc.castaneda_javier.dtos.hotel.*;
import com.jfcc.castaneda_javier.exceptions.ApiException;
import com.jfcc.castaneda_javier.repositories.HotelRepository;
import com.jfcc.castaneda_javier.repositories.HotelRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

//Tests de la clase HotelServiceImpl
class HotelServiceImplTest {

    private HotelRepository hotelRepository;

    private HotelService hotelService;

    //Se mockea el repositorio y se instancia el servicio
    @BeforeEach
    void setUp() {
        hotelRepository = mock(HotelRepositoryImpl.class);
        hotelService = new HotelServiceImpl(hotelRepository);
    }

    //Test para verificar que se carguen los elementos del repositorio
    @Test
    void shouldGetAllHotels() throws ApiException {

        when(hotelRepository.getAllHotels()).thenReturn(getList());

        List<HotelDTO> result = hotelService.getHotels(null, null, null);

        verify(hotelRepository, atLeastOnce()).getAllHotels();
        assertThat(result).isEqualTo(getList());

    }

    //Test para verificar que se carguen los elementos del repositorio y luego se filtren por destino
    @Test
    void shouldGetFilteredHotels() throws ApiException {

        when(hotelRepository.getAllHotels()).thenReturn(getList());

        List<HotelDTO> result = hotelService.getHotels(null, null, "Buenos Aires");

        verify(hotelRepository, atLeastOnce()).getAllHotels();
        assertThat(result).isEqualTo(getList().stream().filter(hotelDTO -> hotelDTO.getCity().equals("Buenos Aires")).collect(Collectors.toList()));

    }

    //Test para verificar que se carguen los elementos del repositorio y luego se filtren por fechas
    @Test
    void shouldGetDateFilteredHotels() throws ApiException {

        when(hotelRepository.getAllHotels()).thenReturn(getList());

        List<HotelDTO> result = hotelService.getHotels("11/03/2021", "15/03/2021", null);

        verify(hotelRepository, atLeastOnce()).getAllHotels();
        assertThat(result).isEqualTo(getList().stream().filter(hotelDTO -> hotelDTO.getAvailableFrom().isBefore(LocalDate.of(2021, 03, 11)) &&
                hotelDTO.getAvailableTo().isAfter(LocalDate.of(2021, 03, 15))).collect(Collectors.toList()));

    }

    //Test que hace una reserva en un hotel
    //No se modifica el repositorio ni los archivos
    //Se usan como entrada y salida un archivo json para cada acción respectivamente
    @Test
    void shouldMakeBooking() throws IOException, ApiException {

        when(hotelRepository.getAllHotels()).thenReturn(getList());
        doNothing().when(hotelRepository).changeState(isA(String.class));

        BookingSolitudeDTO booking = loadBookingRequest("bookingRequest.json");
        TicketBookingOkDTO result = loadBookingTicket("bookingResult.json");

        assertThat(hotelService.makeBooking(booking)).isEqualTo(result);
    }

    //Test que hace una reserva en un hotel con tarjeta débito en otro tipo de habitación
    //No se modifica el repositorio ni los archivos
    //Se usan como entrada y salida un archivo json para cada acción respectivamente
    @Test
    void shouldMakeOtherBooking() throws IOException, ApiException {

        when(hotelRepository.getAllHotels()).thenReturn(getList());
        doNothing().when(hotelRepository).changeState(isA(String.class));

        BookingSolitudeDTO booking = loadBookingRequest("bookingRequestTripleDebit.json");
        TicketBookingOkDTO result = loadBookingTicket("bookingResultTripleDebit.json");

        assertThat(hotelService.makeBooking(booking)).isEqualTo(result);
    }

    private List<HotelDTO> getList() {
        HotelDTO hotel1 = new HotelDTO("CH-0002", "Cataratas Hotel", "Puerto Iguazú", "Doble", (long) 6300, LocalDate.of(2021, 02, 10), LocalDate.of(2021, 03, 20), false);
        HotelDTO hotel2 = new HotelDTO("CH-0003", "Cataratas Hotel 2", "Puerto Iguazú", "Triple", (long) 8200, LocalDate.of(2021, 02, 10), LocalDate.of(2021, 03, 23), false);
        HotelDTO hotel3 = new HotelDTO("HB-0001", "Hotel Bristol", "Buenos Aires", "Single", (long) 5435, LocalDate.of(2021, 02, 10), LocalDate.of(2021, 03, 19), false);
        HotelDTO hotel4 = new HotelDTO("BH-0002", "Hotel Bristol 2", "Buenos Aires", "Doble", (long) 7200, LocalDate.of(2021, 02, 12), LocalDate.of(2021, 04, 17), false);
        HotelDTO hotel5 = new HotelDTO("SH-0002", "Sheraton", "Tucumán", "Doble", (long) 5790, LocalDate.of(2021, 04, 17), LocalDate.of(2021, 05, 23), false);
        HotelDTO hotel6 = new HotelDTO("SH-0001", "Sheraton 2", "Tucumán", "Single", (long) 4150, LocalDate.of(2021, 01, 02), LocalDate.of(2021, 02, 19), false);
        HotelDTO hotel7 = new HotelDTO("SE-0001", "Selina", "Bogotá", "Single", (long) 3900, LocalDate.of(2021, 01, 23), LocalDate.of(2021, 11, 23), false);
        HotelDTO hotel8 = new HotelDTO("SE-0002", "Selina 2", "Bogotá", "Doble", (long) 5840, LocalDate.of(2021, 01, 23), LocalDate.of(2021, 10, 15), false);
        HotelDTO hotel9 = new HotelDTO("EC-0003", "El Campín", "Bogotá", "Triple", (long) 7020, LocalDate.of(2021, 02, 15), LocalDate.of(2021, 03, 27), false);
        HotelDTO hotel10 = new HotelDTO("CP-0004", "Central Plaza", "Medellín", "Múltiple", (long) 8600, LocalDate.of(2021, 03, 01), LocalDate.of(2021, 04, 17), false);
        HotelDTO hotel11 = new HotelDTO("CP-0002", "Central Plaza 2", "Medellín", "Doble", (long) 6400, LocalDate.of(2021, 02, 10), LocalDate.of(2021, 03, 20), false);
        HotelDTO hotel12 = new HotelDTO("BG-0004", "Bocagrande", "Cartagena", "Múltiple", (long) 9370, LocalDate.of(2021, 04, 17), LocalDate.of(2021, 06, 12), false);

        return List.of(hotel1, hotel2, hotel3, hotel4, hotel5, hotel6, hotel7, hotel8, hotel9, hotel10, hotel11, hotel12);
    }

    private BookingSolitudeDTO loadBookingRequest(String fileName) {
        File file = null;

        try {
            file = ResourceUtils.getFile("classpath:" + fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ObjectMapper objectMapper = new ObjectMapper();
        TypeReference<BookingSolitudeDTO> typeReference = new TypeReference<>() {
        };
        BookingSolitudeDTO bookingRequest = null;
        try {
            bookingRequest = objectMapper.readValue(file, typeReference);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bookingRequest;
    }

    private TicketBookingOkDTO loadBookingTicket(String fileName) {
        File file = null;

        try {
            file = ResourceUtils.getFile("classpath:" + fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ObjectMapper objectMapper = new ObjectMapper();
        TypeReference<TicketBookingOkDTO> typeReference = new TypeReference<>() {
        };
        TicketBookingOkDTO ticket = null;
        try {
            ticket = objectMapper.readValue(file, typeReference);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ticket;
    }
}