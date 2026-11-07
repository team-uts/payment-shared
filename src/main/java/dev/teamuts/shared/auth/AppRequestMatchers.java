package dev.teamuts.shared.auth;

import java.util.List;
import java.util.stream.Stream;
import lombok.AllArgsConstructor;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;

@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class AppRequestMatchers {
  private List<AppRequestMatcher> matchers;

  public static AppRequestMatchers of(AppRequestMatcher... matchers) {
    return new AppRequestMatchers(List.of(matchers));
  }

  public AppRequestMatchers merge(AppRequestMatchers other) {
    return new AppRequestMatchers(
        Stream.concat(matchers.stream(), other.matchers.stream()).toList());
  }

  public OrRequestMatcher toOrRequestMatcher() {
    return new OrRequestMatcher(
        matchers.stream()
            .map(matcher ->
                PathPatternRequestMatcher.withDefaults()
                    .matcher(matcher.getHttpMethod(), matcher.getUrlPattern()))
            .toList());
  }
}
