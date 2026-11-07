package dev.teamuts.shared.auth;

import dev.teamuts.shared.auth.constant.RoleType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpMethod;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class AppRequestMatcher {
  private String urlPattern;
  private HttpMethod httpMethod;
  private RoleType role;

  public static AppRequestMatcher noneRoleOf(String urlPattern, HttpMethod httpMethod) {
    return AppRequestMatcher.builder()
        .urlPattern(urlPattern)
        .httpMethod(httpMethod)
        .role(RoleType.NONE)
        .build();
  }

  public static AppRequestMatcher of(String urlPattern, HttpMethod httpMethod, RoleType role) {
    return AppRequestMatcher.builder()
        .urlPattern(urlPattern)
        .httpMethod(httpMethod)
        .role(role)
        .build();
  }
}
