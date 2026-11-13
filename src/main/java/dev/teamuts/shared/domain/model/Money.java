package dev.teamuts.shared.domain.model;

import dev.teamuts.shared.domain.constant.Currency;
import dev.teamuts.shared.domain.error.MoneyErrorCode;
import dev.teamuts.shared.error.exception.BusinessException;
import dev.teamuts.shared.error.exception.ErrorArguments;
import dev.teamuts.shared.utils.BigDecimalUtils;
import java.math.BigDecimal;
import java.math.RoundingMode;
import lombok.*;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class Money {
  @EqualsAndHashCode.Include(rank = 1)
  private final BigDecimal amount;

  @EqualsAndHashCode.Include(rank = 2)
  private final Currency currency;

  public static Money zero(Currency currency) {
    return of(BigDecimal.ZERO, currency);
  }

  public static Money of(BigDecimal amount, Currency currency) {
    if (BigDecimalUtils.isLessThanZero(amount)) {
      throw BusinessException.of(MoneyErrorCode.LESS_THAN_ZERO, ErrorArguments.of(amount));
    }

    return new Money(amount.setScale(currency.getScale(), RoundingMode.UNNECESSARY), currency);
  }

  public boolean isEqual(Money other) {
    checkIfSameCurrency(other);

    return BigDecimalUtils.isEqual(this.amount, other.amount);
  }

  public boolean isNotEqual(Money other) {
    checkIfSameCurrency(other);

    return BigDecimalUtils.isNotEqual(this.amount, other.amount);
  }

  public boolean isGreaterThanZero() {
    return BigDecimalUtils.isGreaterThan(
        this.amount, BigDecimal.ZERO.setScale(currency.getScale(), RoundingMode.UNNECESSARY));
  }

  public boolean isZero() {
    return BigDecimalUtils.isEqual(
        this.amount, BigDecimal.ZERO.setScale(currency.getScale(), RoundingMode.UNNECESSARY));
  }

  public boolean isGreaterThan(Money other) {
    checkIfSameCurrency(other);

    return BigDecimalUtils.isGreaterThan(this.amount, other.amount);
  }

  public boolean isGreaterThanOrEqualTo(Money other) {
    checkIfSameCurrency(other);

    return BigDecimalUtils.isGreaterThanOrEqualTo(this.amount, other.amount);
  }

  public boolean isLessThan(Money other) {
    checkIfSameCurrency(other);

    return BigDecimalUtils.isLessThan(this.amount, other.amount);
  }

  public boolean isLessThanOrEqualTo(Money other) {
    checkIfSameCurrency(other);

    return BigDecimalUtils.isLessThanOrEqualTo(this.amount, other.amount);
  }

  public boolean hasSameCurrency(Money other) {
    return this.currency.equals(other.currency);
  }

  public boolean hasNotSameCurrency(Money other) {
    return !hasSameCurrency(other);
  }

  public Money plus(Money other) {
    checkIfSameCurrency(other);

    return Money.of(BigDecimalUtils.add(this.amount, other.amount), this.currency);
  }

  public Money minus(Money other) {
    checkIfSameCurrency(other);

    return Money.of(BigDecimalUtils.subtract(this.amount, other.amount), this.currency);
  }

  public Money multiplyByPercentage(Percentage percentage) {
    return Money.of(
        amount
            .multiply(percentage.getValue())
            .movePointLeft(Percentage.DECIMAL_PLACES)
            .setScale(currency.getScale(), RoundingMode.HALF_UP),
        this.currency);
  }

  public Money copy() {
    return Money.of(this.amount, this.currency);
  }

  private void checkIfSameCurrency(Money other) {
    if (!this.currency.equals(other.currency)) {
      throw BusinessException.of(
          MoneyErrorCode.CURRENCY_MISMATCH, ErrorArguments.of(this.currency, other.currency));
    }
  }
}
