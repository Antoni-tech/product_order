package kz.symtech.antifraud.authservice.utils;


import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * Данный класс предоставляет функцонал преобразования дат
 */
@Component
public class DateUtil {

    private ZoneId zoneId = ZoneId.systemDefault();

    public Date toDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(zoneId).toInstant());
    }
}
