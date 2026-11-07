package dev.teamuts.shared.error.constant;

public interface SystemErrorCode extends ErrorCode {
  default String getDetailedMessage() {
    return "Unidentified error occurred. Please contact the administrator.";
  }
}
