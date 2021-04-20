package com.jfcc.castaneda_javier.dtos.flight;

import com.jfcc.castaneda_javier.dtos.general.PaymentMethodDTO;
import lombok.Data;

@Data
public class ReservationRequestDTO extends ReservationDTO {

    private PaymentMethodDTO paymentMethod;

}
