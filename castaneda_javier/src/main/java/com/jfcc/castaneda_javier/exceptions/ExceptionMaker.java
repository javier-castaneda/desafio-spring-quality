package com.jfcc.castaneda_javier.exceptions;

import com.jfcc.castaneda_javier.dtos.StatusCodeDTO;

import java.util.HashMap;

public class ExceptionMaker {

    private static HashMap<String, StatusCodeDTO> ERROR_CODES = new HashMap<>(){{
        put("DATE1",new StatusCodeDTO(400, "No se introdujo la fecha correctamente, el formato es dd/mm/yyyy"));
        put("DATE2",new StatusCodeDTO(400, "El intervalo de las fechas está mal, la fecha de entrada debe ser anterior a la fecha de salida"));
        put("DEST1",new StatusCodeDTO(400, "No se encontró el destino, no está disponible o no concuerda con el código"));
        put("ORIG1",new StatusCodeDTO(400, "No se encontró el destino, no está disponible o no concuerda con el código"));
        put("NOHOT1",new StatusCodeDTO(400, "El hotel no tiene disponibilidad o no existe, verifique el código"));
        put("NOHOT2",new StatusCodeDTO(400, "No se encontró hotel disponible para la fecha"));
        put("NOHOT3",new StatusCodeDTO(400, "No se encontró hotel disponible en el destino"));
        put("NOHOT4",new StatusCodeDTO(400, "No se encontró hotel disponible con el código de hotel dado"));
        put("ROOM1",new StatusCodeDTO(400, "No se encontró habitación del tipo especificado"));
        put("MAIL1",new StatusCodeDTO(400, "Formato de email no válido, verifica el campo"));
        put("PPL1",new StatusCodeDTO(400, "La cantidad de personas y el tipo de reserva no coinciden"));
    }};

    public static ApiException getException(String code){
        if(ERROR_CODES.containsKey(code)){
            return new ApiException(ERROR_CODES.get(code));
        }else{
            return new ApiException(new StatusCodeDTO(500, "Error interno no especificado"));
        }
    }

}
