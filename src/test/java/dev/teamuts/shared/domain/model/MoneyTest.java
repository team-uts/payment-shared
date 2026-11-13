package dev.teamuts.shared.domain.model;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import dev.teamuts.shared.domain.constant.Currency;
import java.math.BigDecimal;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class MoneyTest {
  @Test
  void toStringTest() {
    String givenAmount = "100.00";
    Currency givenCurrent = Currency.AUD;

    Money money = Money.of(new BigDecimal(givenAmount), givenCurrent);

    assertThat(money.toString())
        .isEqualTo("Money(amount=%s, currency=%s)".formatted(givenAmount, givenCurrent));
  }

  @ParameterizedTest
  @MethodSource("multiplyByPercentageTestArguments")
  void multiplyByPercentageTest(
      BigDecimal baseAmount,
      BigDecimal givenPercent,
      Currency currency,
      BigDecimal expectedAmount) {
    Money baseMoney = Money.of(baseAmount, currency);
    Percentage percentage = Percentage.of(givenPercent);
    Money result = baseMoney.multiplyByPercentage(percentage);

    assertThat(result.getAmount()).isEqualTo(expectedAmount);
    assertThat(result.getCurrency()).isEqualTo(currency);
  }

  static Stream<Arguments> multiplyByPercentageTestArguments() {
    return Stream.of(
        Arguments.of(
            new BigDecimal("235"), new BigDecimal("11.37"), Currency.AUD, new BigDecimal("26.72")),
        Arguments.of(
            new BigDecimal("8761"),
            new BigDecimal("21.34"),
            Currency.AUD,
            new BigDecimal("1869.60")));
  }
}
