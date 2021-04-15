package com.jfcc.castaneda_javier.dtos.general;

import lombok.Data;

@Data
public class PaymentMethodDTO {
    private String type;
    private String number;
    private int dues;
}
