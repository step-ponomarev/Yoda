package edu.ponomarev.step.manager;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class TimeManager {
  private TimeManager() {};

  public static LocalDateTime getLocalDateTimeOf(final LocalDateTime dateTime) {
    return LocalDateTime.of(
        dateTime.getYear(),
        dateTime.getMonth(),
        dateTime.getDayOfMonth(), dateTime.getHour(),
        dateTime.getMinute(), dateTime.getSecond()
    );
  }

  public static LocalDateTime convertToLocalTimeZone(LocalDateTime dateTimeInUTC) {
    return dateTimeInUTC.atZone(ZoneId.of("UTC"))
        .withZoneSameInstant(ZoneId.systemDefault())
        .toLocalDateTime();
  }
}
