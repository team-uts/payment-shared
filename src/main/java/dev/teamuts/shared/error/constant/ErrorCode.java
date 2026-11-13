package dev.teamuts.shared.error.constant;

import org.jspecify.annotations.NonNull;

public interface ErrorCode extends KtErrorCode {
  String name();

  @Override
  default @NonNull String getName() {
    return name();
  }
}
