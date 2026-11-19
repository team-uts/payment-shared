package dev.teamuts.shared.data;

import dev.teamuts.shared.error.constant.SystemErrorCode;
import lombok.Getter;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;

@Getter
public enum PersistenceErrorCode implements SystemErrorCode {
  NOT_FOUND("PERS-0001"),
  NOT_PERSISTED("PERS-0002"),
  ALREADY_PERSISTED("PERS-0003"),
  NOT_ACQUIRED_ENTITY_LOCK("PERS-0004"),
  ENTITY_LOCK_CHECKING_FAILED("PERS-0005"),
  LOCK_ACQUISITION_FAILED("PERS-0006", true),
  INVALID_LOCK_ANNOTATION("PERS-0007"),
  ;

  private final String code;
  // Persistence layer errors > Internal Server Error (500)
  private final HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
  private final boolean detailEnabled;

  PersistenceErrorCode(String code) {
    this.code = code;
    this.detailEnabled = false; // default
  }

  PersistenceErrorCode(String code, boolean detailEnabled) {
    this.code = code;
    this.detailEnabled = detailEnabled;
  }

  @Override
  public @NonNull String getMessageKey() {
    return "persistence." + name();
  }
}
