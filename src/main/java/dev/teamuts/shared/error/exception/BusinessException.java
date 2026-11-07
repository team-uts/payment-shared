package dev.teamuts.shared.error.exception;

import dev.teamuts.shared.error.constant.ErrorCode;

public class BusinessException extends ApiExternalException {
  protected BusinessException(ErrorCode errorCode) {
    super(errorCode);
  }

  protected BusinessException(ErrorCode errorCode, ErrorArguments errorArgs) {
    super(errorCode, errorArgs);
  }

  public static BusinessException of(ErrorCode errorCode, ErrorArguments errorArgs) {
    return new BusinessException(errorCode, errorArgs);
  }
}
