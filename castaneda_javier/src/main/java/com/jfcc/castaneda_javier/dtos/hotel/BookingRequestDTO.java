package com.jfcc.castaneda_javier.dtos.hotel;

import com.jfcc.castaneda_javier.dtos.general.PaymentMethodDTO;
import lombok.Data;

@Data
public class BookingRequestDTO extends BookingDTO{
    private PaymentMethodDTO paymentMethod;
}
