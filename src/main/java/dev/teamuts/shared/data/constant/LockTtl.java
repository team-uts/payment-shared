package dev.teamuts.shared.data.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public enum LockTtl {
  ZERO(CacheTime.ZERO),
  THREE_SECONDS(CacheTime.THREE_SECONDS),
  FIVE_SECONDS(CacheTime.FIVE_SECONDS),
  TEN_SECONDS(CacheTime.TEN_SECONDS);

  private final CacheTime time;

  public long millis() {
    return time.toMillis();
  }

  public boolean isZero() {
    return this == ZERO;
  }
}
