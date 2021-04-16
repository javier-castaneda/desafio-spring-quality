package com.jfcc.castaneda_javier.exceptions;

import com.jfcc.castaneda_javier.dtos.StatusCodeDTO;
import lombok.Data;

@Data
public class ApiException extends Exception{

    private int code;
    public ApiException(StatusCodeDTO statusCode){
        super(statusCode.getMessage());
        this.code = statusCode.getStatusCode();
    }

}
