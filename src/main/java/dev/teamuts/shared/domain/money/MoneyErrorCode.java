package dev.teamuts.shared.domain.money;

import dev.teamuts.shared.error.constant.ErrorCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum MoneyErrorCode implements ErrorCode {
  LESS_THAN_ZERO("MNY-0001", HttpStatus.BAD_REQUEST),
  CURRENCY_MISMATCH("MNY-0002", HttpStatus.BAD_REQUEST),
  ;

  private final String code;
  private final HttpStatus httpStatus;

  @Override
  public String getMessageKey() {
    return "money." + name();
  }
}
