package com.increff.posapp.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {
    public static String getDateTimeString(ZonedDateTime zonedDateTime, String pattern){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return zonedDateTime.format(formatter);
    }

    public static String getDateTimeString(LocalDateTime localDateTime, String pattern){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return localDateTime.format(formatter);
    }

    public static ZonedDateTime getZonedDateTimeStart(LocalDate localDate, String zone){
        ZoneId zoneId = ZoneId.of(zone);
        LocalDateTime localDateTime = localDate.atTime(0, 0, 0);
        ZonedDateTime zonedDateTime = localDateTime.atZone(zoneId);
        return zonedDateTime;
    }

    public static ZonedDateTime getZonedDateTimeEnd(LocalDate localDate, String zone){
        ZoneId zoneId = ZoneId.of(zone);
        LocalDateTime localDateTime = localDate.atTime(23, 59, 59);
        ZonedDateTime zonedDateTime = localDateTime.atZone(zoneId);
        return zonedDateTime;
    }

}
