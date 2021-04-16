package com.jfcc.castaneda_javier.repositories;

import com.jfcc.castaneda_javier.dtos.hotel.HotelDTO;
import com.jfcc.castaneda_javier.exceptions.ApiException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class HotelRepositoryImplTest {

    private HotelRepository hotelRepository;

    @BeforeEach
    void setUp() throws IOException, ApiException {
        hotelRepository = new HotelRepositoryImpl("HotelesTest.csv");
    }

    @Test
    void getAllHotels() {
        assertThat(hotelRepository.getAllHotels()).isEqualTo(getList());
    }

    @Test
    void changeState() {
    }

    private List<HotelDTO> getList(){
        HotelDTO hotel1 = new HotelDTO("CH-0002","Cataratas Hotel", "Puerto Iguazú", "Doble", (long)6300, LocalDate.of(2021,02,10),LocalDate.of(2021,03,20),false);
        HotelDTO hotel2 = new HotelDTO("CH-0003","Cataratas Hotel 2", "Puerto Iguazú", "Triple", (long)8200, LocalDate.of(2021,02,10),LocalDate.of(2021,03,23),false);
        HotelDTO hotel3 = new HotelDTO("HB-0001","Hotel Bristol", "Buenos Aires", "Single", (long)5435, LocalDate.of(2021,02,10),LocalDate.of(2021,03,19),false);
        HotelDTO hotel4 = new HotelDTO("BH-0002","Hotel Bristol 2", "Buenos Aires", "Doble", (long)7200, LocalDate.of(2021,02,12),LocalDate.of(2021,04,17),false);
        HotelDTO hotel5 = new HotelDTO("SH-0002","Sheraton", "Tucumán", "Doble", (long)5790, LocalDate.of(2021,04,17),LocalDate.of(2021,05,23),false);
        HotelDTO hotel6 = new HotelDTO("SH-0001","Sheraton 2", "Tucumán", "Single", (long)4150, LocalDate.of(2021,01,02),LocalDate.of(2021,02,19),false);
        HotelDTO hotel7 = new HotelDTO("SE-0001","Selina", "Bogotá", "Single", (long)3900, LocalDate.of(2021,01,23),LocalDate.of(2021,11,23),false);
        HotelDTO hotel8 = new HotelDTO("SE-0002","Selina 2", "Bogotá", "Doble", (long)5840, LocalDate.of(2021,01,23),LocalDate.of(2021,10,15),false);
        HotelDTO hotel9 = new HotelDTO("EC-0003","El Campín", "Bogotá", "Triple", (long)7020, LocalDate.of(2021,02,15),LocalDate.of(2021,03,27),false);
        HotelDTO hotel10 = new HotelDTO("CP-0004","Central Plaza", "Medellín", "Múltiple", (long)8600, LocalDate.of(2021,03,01),LocalDate.of(2021,04,17),false);
        HotelDTO hotel11 = new HotelDTO("CP-0002","Central Plaza 2", "Medellín", "Doble", (long)6400, LocalDate.of(2021,02,10),LocalDate.of(2021,03,20),false);
        HotelDTO hotel12 = new HotelDTO("BG-0004","Bocagrande", "Cartagena", "Múltiple", (long)9370, LocalDate.of(2021,04,17),LocalDate.of(2021,06,12),false);

        return List.of(hotel1, hotel2, hotel3, hotel4, hotel5, hotel6, hotel7, hotel8, hotel9, hotel10, hotel11, hotel12);
    }
}