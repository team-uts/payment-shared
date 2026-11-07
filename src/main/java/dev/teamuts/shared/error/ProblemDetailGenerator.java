package dev.teamuts.shared.error;

import dev.teamuts.shared.error.constant.ErrorCode;
import dev.teamuts.shared.error.constant.SystemErrorCode;
import dev.teamuts.shared.error.exception.SystemException;
import jakarta.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.Locale;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.ProblemDetail;

/**
 * @see <a href="https://www.rfc-editor.org/info/rfc9457">RFC 9457</a>
 */
@Builder
@RequiredArgsConstructor
public class ProblemDetailGenerator {
  private final MessageSource messageSource;
  private final String baseUrl;

  public ProblemDetail generate(
      SystemException exception, HttpServletRequest request, Locale locale) {
    SystemErrorCode errorCode = exception.getErrorCode();
    String detail = exception.getDetailedMessage();

    ProblemDetail problem = ProblemDetail.forStatus(errorCode.getHttpStatus());

    problem.setType(getProblemType(errorCode));
    problem.setTitle(errorCode.name());
    problem.setProperty("code", errorCode.getCode());

    problem.setDetail(
        detail != null && !detail.isBlank()
            ? detail
            : messageSource.getMessage(
                errorCode.getMessageKey(), null, errorCode.getDetailedMessage(), locale));

    problem.setInstance(URI.create(request.getRequestURI()));

    return problem;
  }

  private URI getProblemType(ErrorCode errorCode) {
    return URI.create(
        "%s/%s".formatted(baseUrl, errorCode.name().toLowerCase(Locale.ROOT).replace("_", "-")));
  }
}
