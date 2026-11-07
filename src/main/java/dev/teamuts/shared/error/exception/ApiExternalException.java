package dev.teamuts.shared.error.exception;

import dev.teamuts.shared.error.constant.ErrorCode;
import lombok.Getter;

/** It is for error handling to standardize the error response of API. */
@Getter
public class ApiExternalException extends RuntimeException {
  private final ErrorCode errorCode;
  private final ErrorArguments errorArgs;

  protected ApiExternalException(ErrorCode errorCode) {
    super(errorCode.name());
    this.errorCode = errorCode;
    this.errorArgs = ErrorArguments.empty();
  }

  protected ApiExternalException(ErrorCode errorCode, Throwable cause) {
    super(errorCode.name(), cause);
    this.errorCode = errorCode;
    this.errorArgs = ErrorArguments.empty();
  }

  protected ApiExternalException(ErrorCode errorCode, ErrorArguments errorArgs) {
    super(errorCode.name());
    this.errorCode = errorCode;
    this.errorArgs = errorArgs;
  }

  public Object[] getArgs() {
    return errorArgs.getArgs();
  }
}
