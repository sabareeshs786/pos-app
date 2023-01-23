package com.increff.posapp.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {
    public static String getDateTime(ZonedDateTime zonedDateTime, String pattern){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return zonedDateTime.format(formatter);
    }

    public static ZonedDateTime getZonedDateTimeIndia(){
        LocalDateTime localDateTime = LocalDateTime.now();
        ZoneId india = ZoneId.of("Asia/Kolkata");
        ZonedDateTime zoneIndia = ZonedDateTime.of(localDateTime, india);
        return zoneIndia;
    }
}
