package dev.teamuts.shared.error.constant;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum CommonErrorCode implements SystemErrorCode {
  INTERNAL_SERVER_ERROR("COM-0001", HttpStatus.INTERNAL_SERVER_ERROR),
  ;

  private final String code;
  private final HttpStatus httpStatus;

  @Override
  public @NonNull String getMessageKey() {
    return "common." + name();
  }
}
