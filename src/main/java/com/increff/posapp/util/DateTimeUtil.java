package com.increff.posapp.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {
    public static String getDateTimeString(ZonedDateTime zonedDateTime, String pattern){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return zonedDateTime.format(formatter);
    }

    public static ZonedDateTime getZonedDateTime(String zone){
        LocalDateTime localDateTime = LocalDateTime.now();
        ZoneId zoneId = ZoneId.of(zone);
        return ZonedDateTime.of(localDateTime, zoneId);
    }

    public static String getZonedDateTimeIndiaAsString(){
        LocalDateTime currentTime = LocalDateTime.now();
        ZoneId india = ZoneId.of("Asia/Kolkata");
        ZonedDateTime zoneIndia = ZonedDateTime.of(currentTime, india);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return currentTime.format(dateTimeFormatter);
    }

}
