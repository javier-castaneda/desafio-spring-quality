package com.jfcc.castaneda_javier.repositories;

import com.jfcc.castaneda_javier.dtos.hotel.HotelDTO;

import java.io.IOException;
import java.util.List;

public interface HotelRepository {

    public List<HotelDTO> getAllHotels();
    public void changeState(String hotelCode) throws IOException;
}
