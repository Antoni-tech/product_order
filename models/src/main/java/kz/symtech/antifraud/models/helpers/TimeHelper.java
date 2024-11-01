package kz.symtech.antifraud.models.helpers;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static java.lang.Integer.parseInt;

public class TimeHelper {

   public static Instant convertTime(String time) {
      String[] timeSplit = time.split(":");
      return LocalDateTime.now()
              .withHour(parseInt(timeSplit[0]))
              .withMinute(parseInt(timeSplit[1]))
              .atZone(
              ZoneOffset.UTC
      ).toInstant();
   }
   public static String convertTimeFromInstant(Instant time) {
      int hour = time.atZone(ZoneOffset.UTC).getHour();
      int minutes = time.atZone(ZoneOffset.UTC).getMinute();
      return String.valueOf((hour < 10 ? "0" + hour : hour)
              + ":"
              + (minutes < 10 ? "0" + minutes : minutes));
   }
}
