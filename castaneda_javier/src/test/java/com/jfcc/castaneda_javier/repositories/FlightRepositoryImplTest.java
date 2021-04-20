package com.jfcc.castaneda_javier.repositories;

import com.jfcc.castaneda_javier.dtos.flight.FlightDTO;
import com.jfcc.castaneda_javier.exceptions.ApiException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

//Tests de la clase FLightRepositoryImpl
class FlightRepositoryImplTest {

    private FlightRepository flightRepository;

    //Se usa un archivo de Vuelos diferente al que se usa en "producción"
    @BeforeEach
    void setUp() throws IOException, ApiException {
        flightRepository = new FlightRepositoryImpl("VuelosTest.csv");
    }

    //Se prueba que se carguen los datos igual a los que se encuentran hardcoded en el método getList
    @Test
    void getAllFlights() {

        assertThat(flightRepository.getAllFlights()).isEqualTo(getList());

    }

    private List<FlightDTO> getList() {

        FlightDTO flight1 = new FlightDTO("BAPI-1235", "Buenos Aires", "Puerto Iguazú", "Economy", (long) 6500, LocalDate.of(2021, 02, 10), LocalDate.of(2021, 02, 15));
        FlightDTO flight2 = new FlightDTO("PIBA-1420", "Puerto Iguazú", "Bogotá", "Business", (long) 43200, LocalDate.of(2021, 02, 10), LocalDate.of(2021, 02, 20));
        FlightDTO flight3 = new FlightDTO("PIBA-1420", "Puerto Iguazú", "Bogotá", "Economy", (long) 25735, LocalDate.of(2021, 02, 10), LocalDate.of(2021, 02, 21));
        FlightDTO flight4 = new FlightDTO("BATU-5536", "Buenos Aires", "Tucumán", "Economy", (long) 7320, LocalDate.of(2021, 02, 10), LocalDate.of(2021, 02, 17));
        FlightDTO flight5 = new FlightDTO("TUPI-3369", "Tucumán", "Puerto Iguazú", "Business", (long) 12530, LocalDate.of(2021, 02, 12), LocalDate.of(2021, 02, 23));
        FlightDTO flight6 = new FlightDTO("TUPI-3369", "Tucumán", "Puerto Iguazú", "Economy", (long) 5400, LocalDate.of(2021, 01, 02), LocalDate.of(2021, 01, 16));
        FlightDTO flight7 = new FlightDTO("BOCA-4213", "Bogotá", "Cartagena", "Economy", (long) 8000, LocalDate.of(2021, 01, 23), LocalDate.of(2021, 02,05));
        FlightDTO flight8 = new FlightDTO("CAME-0321", "Cartagena", "Medellín", "Economy", (long) 7800, LocalDate.of(2021, 01, 23), LocalDate.of(2021, 01, 31));
        FlightDTO flight9 = new FlightDTO("BOBA-6567", "Bogotá", "Buenos Aires", "Business", (long) 57000, LocalDate.of(2021, 02, 15), LocalDate.of(2021, 02, 28));
        FlightDTO flight10 = new FlightDTO("BOBA-6567", "Bogotá", "Buenos Aires", "Economy", (long) 39860, LocalDate.of(2021, 03, 01), LocalDate.of(2021, 03, 14));
        FlightDTO flight11 = new FlightDTO("BOME-4442", "Bogotá", "Medellín", "Economy", (long) 11000, LocalDate.of(2021, 02, 10), LocalDate.of(2021, 02, 24));
        FlightDTO flight12 = new FlightDTO("MEPI-9986", "Medellín", "Puerto Iguazú", "Business", (long) 41640, LocalDate.of(2021, 04, 17), LocalDate.of(2021, 05, 02));

        return List.of(flight1, flight2, flight3, flight4, flight5, flight6, flight7, flight8, flight9, flight10, flight11, flight12);

    }
}