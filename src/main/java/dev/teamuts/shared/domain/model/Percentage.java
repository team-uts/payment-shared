package dev.teamuts.shared.domain.model;

import dev.teamuts.shared.domain.error.PercentageErrorCode;
import dev.teamuts.shared.error.exception.BusinessException;
import dev.teamuts.shared.utils.BigDecimalUtils;
import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@EqualsAndHashCode
public class Percentage {
  private final BigDecimal value;

  public static final BigDecimal HUNDRED = new BigDecimal("100.00");

  public static Percentage of(BigDecimal value) {
    if (BigDecimalUtils.isGreaterThanEqualToZero(value)
        && BigDecimalUtils.isLessThanEqualTo(value, HUNDRED)) {
      throw BusinessException.of(PercentageErrorCode.INVALID);
    }

    return new Percentage(value);
  }
}
