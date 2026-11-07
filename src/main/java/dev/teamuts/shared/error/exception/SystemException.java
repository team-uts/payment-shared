package dev.teamuts.shared.error.exception;

import dev.teamuts.shared.error.constant.SystemErrorCode;
import lombok.Getter;

@Getter
public class SystemException extends ApiExternalException {
  private final String logMessage;
  private final String detailedMessage;

  protected SystemException(SystemErrorCode errorCode, String logMessage, String detailedMessage) {
    super(errorCode);
    this.logMessage = logMessage;
    this.detailedMessage = detailedMessage;
  }

  protected SystemException(
      SystemErrorCode errorCode, Throwable cause, String logMessage, String detailedMessage) {
    super(errorCode, cause);
    this.logMessage = logMessage;
    this.detailedMessage = detailedMessage;
  }

  protected SystemException(
      SystemErrorCode errorCode,
      ErrorArguments errorArgs,
      String logMessage,
      String detailedMessage) {
    super(errorCode, errorArgs);
    this.logMessage = logMessage;
    this.detailedMessage = detailedMessage;
  }

  public static SystemException of(SystemErrorCode errorCode) {
    return new SystemException(errorCode, null, null);
  }

  public static SystemException fromOrDefault(Throwable cause, SystemErrorCode defaultCode) {
    if (cause instanceof SystemException systemException) {
      return systemException;
    }

    return new SystemException(defaultCode, cause, null, null);
  }

  public static SystemException of(SystemErrorCode errorCode, ErrorArguments errorArgs) {
    return new SystemException(errorCode, errorArgs, null, null);
  }

  @Override
  public SystemErrorCode getErrorCode() {
    return (SystemErrorCode) super.getErrorCode();
  }
}
