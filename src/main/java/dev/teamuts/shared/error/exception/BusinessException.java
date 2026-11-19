package dev.teamuts.shared.error.exception;

import dev.teamuts.shared.error.constant.KtErrorCode;
import dev.teamuts.shared.http.response.ApiCommonResponse;

/**
 * This exception is caused by business logic error in each domain<br>
 * (e.g., paying order process with invalid payment method)<br>
 * This will be mapped to {@link ApiCommonResponse} with HTTP status code 200.
 *
 * @author <a href="https://github.com/joshua-hb-lee">Joshua (Hanbin) Lee</a>
 */
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
