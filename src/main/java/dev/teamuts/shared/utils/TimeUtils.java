package dev.teamuts.shared.utils;

public class TimeUtils {
  public static final long MILLI_SEC_UNIT = 1_000L;

  public static long convertSecToMillis(int seconds) {
    return seconds * MILLI_SEC_UNIT;
  }

  public static double convertMillisToSecWithPrecision(long millis) {
    return millis / (double) MILLI_SEC_UNIT;
  }
}
