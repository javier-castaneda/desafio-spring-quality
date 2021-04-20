package com.jfcc.castaneda_javier.repositories;

import com.jfcc.castaneda_javier.dtos.flight.FlightDTO;
import com.jfcc.castaneda_javier.dtos.hotel.HotelDTO;
import com.jfcc.castaneda_javier.exceptions.ApiException;
import com.jfcc.castaneda_javier.utils.DateUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class FlightRepositoryImpl implements FlightRepository {

    private String fileName;
    private List<FlightDTO> flightList;

    public FlightRepositoryImpl(@Value("${flights:Vuelos.csv}") String fileName) throws IOException, ApiException {
        this.fileName = fileName;
        flightList = loadDataBase(fileName);
    }


    @Override
    public List<FlightDTO> getAllFlights() {
        return flightList;
    }

    private List<FlightDTO> loadDataBase(String fileName) throws ApiException, IOException {
        List<FlightDTO> flightDTOS = new ArrayList<>();
        int count = 0;
        String line = "";
        FileReader fr = new FileReader(getClass().getClassLoader().getResource(fileName).getFile());
        BufferedReader br = new BufferedReader(fr);
        while ((line = br.readLine()) != null) {
            if (count > 0) {
                FlightDTO flight = new FlightDTO();
                String[] dbHotel = line.split(",");
                flight.setFlightNumber(dbHotel[0]);
                flight.setOrigin(dbHotel[1]);
                flight.setDestination(dbHotel[2]);
                flight.setSeatType(dbHotel[3]);
                flight.setPriceByPerson(Long.parseLong(dbHotel[4].replace("$", "").replace(".", "")));
                flight.setDateFrom(DateUtils.makeLocalDate(dbHotel[5]));
                flight.setDateTo(DateUtils.makeLocalDate(dbHotel[6]));
                flightDTOS.add(flight);
            }
            count++;
        }
        br.close();
        fr.close();
        return flightDTOS;
    }
}
