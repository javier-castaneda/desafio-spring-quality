package com.jfcc.castaneda_javier.repositories;

import com.jfcc.castaneda_javier.dtos.hotel.HotelDTO;
import com.jfcc.castaneda_javier.exceptions.ApiException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

//Tests de la clase HotelRepositoryImpl
class HotelRepositoryImplTest {

    private HotelRepository hotelRepository;

    //Se usa un archivo de Vuelos diferente al que se usa en "producción"
    @BeforeEach
    void setUp() throws IOException, ApiException {
        hotelRepository = new HotelRepositoryImpl("HotelesTest.csv");
    }

    //Se vuelve a dejar el archivo usado en el estado inical para poder repetir los test
    @AfterEach
    void restartFile() throws IOException {

        String hotelStrings = "Código Hotel,Nombre,Lugar/Ciudad,Tipo de Habitación,Precio por noche,Disponible Desde,Disponible hasta,Reservado\n" +
                "CH-0002,Cataratas Hotel,Puerto Iguazú,Doble,$6300,10/02/2021,20/03/2021,NO\n" +
                "CH-0003,Cataratas Hotel 2,Puerto Iguazú,Triple,$8200,10/02/2021,23/03/2021,NO\n" +
                "HB-0001,Hotel Bristol,Buenos Aires,Single,$5435,10/02/2021,19/03/2021,NO\n" +
                "BH-0002,Hotel Bristol 2,Buenos Aires,Doble,$7200,12/02/2021,17/04/2021,NO\n" +
                "SH-0002,Sheraton,Tucumán,Doble,$5790,17/04/2021,23/05/2021,NO\n" +
                "SH-0001,Sheraton 2,Tucumán,Single,$4150,02/01/2021,19/02/2021,NO\n" +
                "SE-0001,Selina,Bogotá,Single,$3900,23/01/2021,23/11/2021,NO\n" +
                "SE-0002,Selina 2,Bogotá,Doble,$5840,23/01/2021,15/10/2021,NO\n" +
                "EC-0003,El Campín,Bogotá,Triple,$7020,15/02/2021,27/03/2021,NO\n" +
                "CP-0004,Central Plaza,Medellín,Múltiple,$8600,01/03/2021,17/04/2021,NO\n" +
                "CP-0002,Central Plaza 2,Medellín,Doble,$6400,10/02/2021,20/03/2021,NO\n" +
                "BG-0004,Bocagrande,Cartagena,Múltiple,$9370,17/04/2021,12/06/2021,NO";

        FileWriter fw = new FileWriter(getClass().getClassLoader().getResource("HotelesTest.csv").getFile());
        fw.write(hotelStrings);

        fw.close();
    }

    //Se prueba que se carguen los datos igual a los que se encuentran hardcoded en el método getList
    @Test
    void getAllHotels() {
        assertThat(hotelRepository.getAllHotels()).isEqualTo(getList());
    }

    //Se prueba que se cambie el estado en el hotel indicado en memoria, al mismo tiempo que se cambia en el archivo
    @Test
    void changeState() throws IOException, ApiException {

        hotelRepository.changeState("BG-0004");

        List<HotelDTO> resultMod = hotelRepository.getAllHotels();

        assertThat(resultMod).isEqualTo(getModList());

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

    private List<HotelDTO> getModList(){
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
        HotelDTO hotel12 = new HotelDTO("BG-0004","Bocagrande", "Cartagena", "Múltiple", (long)9370, LocalDate.of(2021,04,17),LocalDate.of(2021,06,12),true);

        return List.of(hotel1, hotel2, hotel3, hotel4, hotel5, hotel6, hotel7, hotel8, hotel9, hotel10, hotel11, hotel12);
    }
}