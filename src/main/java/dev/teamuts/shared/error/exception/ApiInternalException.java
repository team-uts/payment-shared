package dev.teamuts.shared.error.exception;

/**
 * Only for app internal use.
 *
 * @author <a href="https://github.com/joshua-hb-lee">Joshua (Hanbin) Lee</a>
 */
public class ApiInternalException extends RuntimeException {
  protected ApiInternalException(String message) {
    super(message);
  }
}
