package dev.teamuts.shared.domain.model;

import dev.teamuts.shared.domain.error.PercentageErrorCode;
import dev.teamuts.shared.error.exception.BusinessException;
import dev.teamuts.shared.utils.BigDecimalUtils;
import java.math.BigDecimal;
import java.math.RoundingMode;
import lombok.*;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@EqualsAndHashCode
public class Percentage {
  private final BigDecimal value;

  public static final Percentage ONE_HUNDRED = new Percentage(new BigDecimal("100.00"));
  public static final int SCALE = 2;
  public static final int DECIMAL_PLACES = 2;

  public static Percentage of(@NonNull BigDecimal value) {
    if (BigDecimalUtils.isLessThanZero(value)
        && BigDecimalUtils.isGreaterThan(value, ONE_HUNDRED.value)) {
      throw BusinessException.of(PercentageErrorCode.INVALID);
    }

    return new Percentage(value.setScale(SCALE, RoundingMode.UNNECESSARY));
  }
}
