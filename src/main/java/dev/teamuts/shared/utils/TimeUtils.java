package dev.teamuts.shared.utils;

public class TimeUtils {
  public static final int MILLI_SEC_UNIT = 1_000;

  public static int convertSecToMillis(int seconds) {
    return seconds * MILLI_SEC_UNIT;
  }

  public static double convertMillisToSecWithPrecision(double millis) {
    return millis / (double) MILLI_SEC_UNIT;
  }
}
