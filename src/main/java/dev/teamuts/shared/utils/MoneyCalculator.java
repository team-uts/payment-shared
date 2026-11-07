package dev.teamuts.shared.utils;

import java.math.BigDecimal;
import java.util.Objects;

public class MoneyCalculator {
  public static boolean isGreaterThan(BigDecimal amount1, BigDecimal amount2) {
    Objects.requireNonNull(amount1);
    Objects.requireNonNull(amount2);
    return amount1.compareTo(amount2) > 0;
  }

  public static boolean isLessThan(BigDecimal amount1, BigDecimal amount2) {
    Objects.requireNonNull(amount1);
    Objects.requireNonNull(amount2);
    return amount1.compareTo(amount2) < 0;
  }

  public static boolean isGreaterThanZero(BigDecimal amount) {
    Objects.requireNonNull(amount);
    return amount.compareTo(BigDecimal.ZERO) > 0;
  }

  public static boolean isLessThanZero(BigDecimal amount) {
    Objects.requireNonNull(amount);
    return amount.compareTo(BigDecimal.ZERO) < 0;
  }

  public static boolean isLessThanEqualToZero(BigDecimal amount) {
    Objects.requireNonNull(amount);
    return amount.compareTo(BigDecimal.ZERO) <= 0;
  }

  public static boolean isEqual(BigDecimal amount1, BigDecimal amount2) {
    Objects.requireNonNull(amount1);
    Objects.requireNonNull(amount2);
    return amount1.compareTo(amount2) == 0;
  }

  public static boolean isNotEqual(BigDecimal amount1, BigDecimal amount2) {
    Objects.requireNonNull(amount1);
    Objects.requireNonNull(amount2);
    return !isEqual(amount1, amount2);
  }

  public static BigDecimal add(BigDecimal amount1, BigDecimal amount2) {
    Objects.requireNonNull(amount1);
    Objects.requireNonNull(amount2);
    return amount1.add(amount2);
  }

  public static BigDecimal subtract(BigDecimal amount1, BigDecimal amount2) {
    Objects.requireNonNull(amount1);
    Objects.requireNonNull(amount2);
    return amount1.subtract(amount2);
  }
}
