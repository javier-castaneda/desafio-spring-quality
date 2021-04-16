package com.jfcc.castaneda_javier.repositories;

import com.jfcc.castaneda_javier.dtos.hotel.HotelDTO;
import com.jfcc.castaneda_javier.exceptions.ApiException;
import com.jfcc.castaneda_javier.utils.DateUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class HotelRepositoryImpl implements HotelRepository {
    private List<HotelDTO> hotelsList;
    private String fileName;


    public HotelRepositoryImpl(@Value("${hotels:Hoteles.csv}") String fileName) throws IOException, ApiException {
        this.fileName = fileName;
        hotelsList = loadDataBase(fileName);
    }

    @Override
    public List<HotelDTO> getAllHotels() {
        return hotelsList;
    }

    @Override
    public void changeState(String hotelCode) throws IOException {
        for (HotelDTO hotel:hotelsList) {
            if(hotel.getCode().equals(hotelCode)){
                hotel.setBooked(true);
            }
        }
        updateDatabase();
    }

    private void updateDatabase() throws IOException {
        String productStrings = "Código Hotel,Nombre,Lugar/Ciudad,Tipo de Habitación,Precio por noche,Disponible Desde,Disponible hasta,Reservado" + System.lineSeparator();
        for (HotelDTO hotel : hotelsList) {
            productStrings += toLine(hotel);
        }

        FileWriter fw = new FileWriter(getClass().getClassLoader().getResource(fileName).getFile());
        fw.write(productStrings);

        fw.close();
    }

    private List<HotelDTO> loadDataBase(String fileName) throws IOException, ApiException {
        List<HotelDTO> hotelDTOS = new ArrayList<>();
        int count = 0;
        String line = "";
        FileReader fr = new FileReader(getClass().getClassLoader().getResource(fileName).getFile());
        BufferedReader br = new BufferedReader(fr);
        while ((line = br.readLine()) != null) {
            if (count > 0) {
                HotelDTO hotel = new HotelDTO();
                String[] dbHotel = line.split(",");
                hotel.setCode(dbHotel[0]);
                hotel.setName(dbHotel[1]);
                hotel.setCity(dbHotel[2]);
                hotel.setRoomType(dbHotel[3]);
                hotel.setPriceByNight(Long.parseLong(dbHotel[4].replace("$", "")));
                hotel.setAvailableFrom(DateUtils.makeLocalDate(dbHotel[5]));
                hotel.setAvailableTo(DateUtils.makeLocalDate(dbHotel[6]));
                hotel.setBooked(dbHotel[7] == "SI");
                hotelDTOS.add(hotel);
            }
            count++;
        }
        br.close();
        fr.close();
        return hotelDTOS;
    }

    private String toLine(HotelDTO hotel) {
        String line = "";
        line += hotel.getCode();
        line += "," + hotel.getName();
        line += "," + hotel.getCity();
        line += "," + hotel.getRoomType();
        line += ",$" + hotel.getPriceByNight();
        line += "," + DateUtils.toFormat(hotel.getAvailableFrom());
        line += "," + DateUtils.toFormat(hotel.getAvailableTo());
        if (hotel.getBooked()) {
            line += ",SI";
        } else {
            line += ",NO";
        }
        line += System.lineSeparator();
        return line;
    }
}
