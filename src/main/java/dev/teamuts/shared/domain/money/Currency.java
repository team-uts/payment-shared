package dev.teamuts.shared.domain.money;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
@Getter
public enum Currency {
  AUD(2),
  USD(2),
  KRW(0);

  private final int scale;
}
