package dev.teamuts.shared.error.exception;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorArguments {
  private final Object[] args;

  public static ErrorArguments empty() {
    return new ErrorArguments(new Object[0]);
  }

  public static ErrorArguments of(Object... args) {
    return new ErrorArguments(args);
  }

  public Object[] getArgs() {
    return args.clone();
  }
}
