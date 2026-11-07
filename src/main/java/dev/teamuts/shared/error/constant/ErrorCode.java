package dev.teamuts.shared.error.constant;

import org.springframework.http.HttpStatus;

public interface ErrorCode {
  String name();

  String getCode();

  // TODO: refactoring
  String getMessageKey();

  HttpStatus getHttpStatus();
}
