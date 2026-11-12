package dev.teamuts.shared.utils;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.TimeZone;

public class AppTimeUtils {
  public static String GLOBAL_DATETIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
  public static final long MILLI_SEC_UNIT = 1_000L;
  public static ZoneOffset GLOBAL_ZONE_OFFSET = ZoneOffset.UTC;

  public static long convertSecToMillis(int seconds) {
    return seconds * MILLI_SEC_UNIT;
  }

  public static double convertMillisToSecWithPrecision(long millis) {
    return millis / (double) MILLI_SEC_UNIT;
  }

  public static void setDefaultTimezone() {
    TimeZone.setDefault(TimeZone.getTimeZone(GLOBAL_ZONE_OFFSET));
  }

  public static ZonedDateTime getNowTime() {
    return ZonedDateTime.now(GLOBAL_ZONE_OFFSET).truncatedTo(ChronoUnit.MICROS);
  }
}
