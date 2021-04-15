package com.jfcc.castaneda_javier.utils;

import com.jfcc.castaneda_javier.exceptions.date.WrongDateFormatException;

import java.time.LocalDate;

public class DateUtils {

    public static LocalDate makeLocalDate(String date) throws WrongDateFormatException {
        String[] day = date.split("/");

        if(day.length!=3){
            throw new WrongDateFormatException(date);
        }

        int dayOfMonth = Integer.parseInt(day[0]);
        int month = Integer.parseInt(day[1]);
        int year = Integer.parseInt(day[2]);

        LocalDate localDate = LocalDate.of(year,month,dayOfMonth);

        if(dayOfMonth!=localDate.getDayOfMonth()||month!=localDate.getMonthValue()||year!=localDate.getYear()){
            throw new WrongDateFormatException(date);
        }
        return localDate;
    }

    public static String toFormat(LocalDate date){
        return date.getDayOfMonth()+"/"+date.getMonthValue()+"/"+date.getYear();
    }

    public static boolean isBetween(LocalDate comparable, LocalDate start, LocalDate end){
        return (comparable.isAfter(start)||comparable.isEqual(start)) && (comparable.isBefore(end)|| comparable.isEqual(end));
    }

}
