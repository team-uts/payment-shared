package dev.teamuts.shared.domain.error;

import dev.teamuts.shared.error.constant.ErrorCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum PercentageErrorCode implements ErrorCode {
  INVALID("PCT-0001", HttpStatus.BAD_REQUEST),
  ;

  private final String code;
  private final HttpStatus httpStatus;

  @Override
  public @NonNull String getMessageKey() {
    return "percentage." + name();
  }
}
