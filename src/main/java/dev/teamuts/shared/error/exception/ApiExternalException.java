package dev.teamuts.shared.error.exception;

import dev.teamuts.shared.error.constant.KtErrorCode;
import lombok.Getter;

/**
 * It is for error handling to standardize the error response of API.
 *
 * @author <a href="https://github.com/joshua-hb-lee">Joshua (Hanbin) Lee</a>
 */
@Getter
public class ApiExternalException extends RuntimeException {
  private final KtErrorCode errorCode;
  private final ErrorArguments errorArgs;

  protected ApiExternalException(KtErrorCode errorCode) {
    super(errorCode.getName());
    this.errorCode = errorCode;
    this.errorArgs = ErrorArguments.empty();
  }

  protected ApiExternalException(KtErrorCode errorCode, Throwable cause) {
    super(errorCode.getName(), cause);
    this.errorCode = errorCode;
    this.errorArgs = ErrorArguments.empty();
  }

  protected ApiExternalException(KtErrorCode errorCode, ErrorArguments errorArgs) {
    super(errorCode.getName());
    this.errorCode = errorCode;
    this.errorArgs = errorArgs;
  }

  public Object[] getArgs() {
    return errorArgs.getArgs();
  }

  // get log message for logging purpose inside.
  public String getLogMessage() {
    if (this instanceof SystemException sysException) {
      return sysException.getLogMessage();
    }

    return this.getMessage();
  }
}
