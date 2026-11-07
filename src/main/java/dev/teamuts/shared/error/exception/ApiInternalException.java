package dev.teamuts.shared.error.exception;

/** Only for app internal use. */
public class ApiInternalException extends RuntimeException {
  protected ApiInternalException(String message) {
    super(message);
  }
}
