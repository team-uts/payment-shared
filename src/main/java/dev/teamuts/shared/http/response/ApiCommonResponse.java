package dev.teamuts.shared.http.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiCommonResponse<T> {
  private final ApiResultType result;
  private final T data;
  private final ErrorResponse error;

  public static <T> ApiCommonResponse<T> success(T data) {
    return new ApiCommonResponse<>(ApiResultType.SUCCESS, data, null);
  }

  public static <T> ApiCommonResponse<T> failure(ErrorResponse errorResponse) {
    return new ApiCommonResponse<>(ApiResultType.FAILURE, null, errorResponse);
  }
}
