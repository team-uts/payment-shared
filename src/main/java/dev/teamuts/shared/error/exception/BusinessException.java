package dev.teamuts.shared.error.exception;

import dev.teamuts.shared.error.constant.KtErrorCode;

public class BusinessException extends ApiExternalException {
  protected BusinessException(KtErrorCode errorCode) {
    super(errorCode);
  }

  protected BusinessException(KtErrorCode errorCode, ErrorArguments errorArgs) {
    super(errorCode, errorArgs);
  }

  public static BusinessException of(KtErrorCode errorCode) {
    return new BusinessException(errorCode);
  }

  public static BusinessException of(KtErrorCode errorCode, ErrorArguments errorArgs) {
    return new BusinessException(errorCode, errorArgs);
  }
}
