package com.mhsolution.noronapi.service.utils;

import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

@Component
public class TimeUtils {

    private static TimeUtils INSTANCE;

    public static TimeUtils getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new TimeUtils();
        }
        return INSTANCE;
    }

    public static LocalDateTime getCurrentDateTime() {
        Date date = new Date();
        Timestamp timestamp = new Timestamp(date.getTime());
        return timestamp.toLocalDateTime();
    }

}
