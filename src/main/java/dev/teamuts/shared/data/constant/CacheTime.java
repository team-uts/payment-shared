package dev.teamuts.shared.data.constant;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public enum CacheTime {
  ZERO(0, ChronoUnit.NANOS),
  THREE_SECONDS(3, ChronoUnit.SECONDS),
  FIVE_SECONDS(5, ChronoUnit.SECONDS),
  TEN_SECONDS(10, ChronoUnit.SECONDS),
  THIRTY_SECONDS(30, ChronoUnit.SECONDS),
  ONE_MINUTE(1, ChronoUnit.MINUTES),
  FIVE_MINUTES(5, ChronoUnit.MINUTES),
  TEN_MINUTES(10, ChronoUnit.MINUTES),
  ONE_HOUR(1L, ChronoUnit.HOURS),
  FIVE_HOURS(5L, ChronoUnit.HOURS),
  SIX_HOURS(6L, ChronoUnit.HOURS),
  ONE_WEEK(7L, ChronoUnit.DAYS);

  private final long time;
  private final ChronoUnit timeUnit;

  public Duration duration() {
    return Duration.of(time, timeUnit);
  }

  public long toMillis() {
    return duration().toMillis();
  }
}
