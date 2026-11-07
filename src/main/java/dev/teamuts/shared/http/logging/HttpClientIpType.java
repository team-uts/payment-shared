package dev.teamuts.shared.http.logging;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import lombok.Getter;
import org.springframework.util.StringUtils;

@Getter
public enum HttpClientIpType {
  X_FORWARDED_FOR("X-Forwarded-For"),
  PROXY_CLIENT_IP("Proxy-Client-IP"),
  WL_PROXY_CLIENT_IP("WL-Proxy-Client-IP"),
  HTTP_X_FORWARDED(""),
  HTTP_X_FORWARDED_FOR(""),
  HTTP_CLIENT_IP(""),
  X_REAL_IP("X-Real-IP", Integer.MAX_VALUE);

  private final String headerName;
  private final int priority;

  HttpClientIpType(String headerName) {
    this.headerName = headerName;
    this.priority = 0;
  }

  HttpClientIpType(String headerName, int priority) {
    this.headerName = headerName;
    this.priority = priority;
  }

  private static final String UNKNOWN_CLIENT = "unknown";

  private static final List<HttpClientIpType> SORTED_PRIORITY_CLIENT_IP_TYPES =
      Arrays.stream(HttpClientIpType.values())
          .sorted(Comparator.comparingInt(HttpClientIpType::getPriority).reversed())
          .toList();

  public static String getClientIpInHeader(HttpServletRequest request) {
    for (HttpClientIpType clientIpType : SORTED_PRIORITY_CLIENT_IP_TYPES) {
      String headerValue = request.getHeader(clientIpType.getHeaderName());

      if (StringUtils.hasText(headerValue) && !UNKNOWN_CLIENT.equalsIgnoreCase(headerValue)) {
        return headerValue;
      }
    }

    return Objects.requireNonNullElse(request.getRemoteAddr(), UNKNOWN_CLIENT);
  }
}
