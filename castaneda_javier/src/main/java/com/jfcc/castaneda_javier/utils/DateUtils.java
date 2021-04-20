package com.jfcc.castaneda_javier.utils;

import com.jfcc.castaneda_javier.exceptions.ApiException;
import com.jfcc.castaneda_javier.exceptions.ExceptionMaker;

import java.time.LocalDate;

public class DateUtils {

    //Parseo de String tipo DD/MM/YYYY a LocalDate
    public static LocalDate makeLocalDate(String date) throws ApiException {
        String[] day = date.split("/");

        if (day.length != 3 || day[0].length() != 2 || day[1].length() != 2 || day[2].length() > 4) {
            throw ExceptionMaker.getException("DATE1");
        }

        int dayOfMonth = Integer.parseInt(day[0]);
        int month = Integer.parseInt(day[1]);
        int year = Integer.parseInt(day[2]);

        LocalDate localDate = LocalDate.of(year, month, dayOfMonth);

        if (dayOfMonth != localDate.getDayOfMonth() || month != localDate.getMonthValue() || year != localDate.getYear()) {
            throw ExceptionMaker.getException("DATE1");
        }
        return localDate;
    }

    //Parseo de LocalDate a String en formato DD/MM/YYYY
    public static String toFormat(LocalDate date) {
        String day = "";
        String month = "";
        if (date.getDayOfMonth() < 10) {
            day += "0";
        }
        if (date.getMonthValue() < 10) {
            month += "0";
        }
        return day + date.getDayOfMonth() + "/" + month + date.getMonthValue() + "/" + date.getYear();
    }

    //Verifica si una fecha está entre otras dos
    public static boolean isBetween(LocalDate comparable, LocalDate start, LocalDate end) {
        return (comparable.isAfter(start) || comparable.isEqual(start)) && (comparable.isBefore(end) || comparable.isEqual(end));
    }

}
