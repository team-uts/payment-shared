package dev.teamuts.shared.data;

import dev.teamuts.shared.domain.model.BaseIdentifiableModel;
import dev.teamuts.shared.error.constant.SystemErrorCode;
import dev.teamuts.shared.error.exception.SystemException;

public class PersistenceException extends SystemException {
  private PersistenceException(
      SystemErrorCode errorCode, String logMessage, String detailedMessage) {
    super(errorCode, logMessage, detailedMessage);
  }

  public static PersistenceException notFound(BaseIdentifiableModel<Long> model) {
    return new PersistenceException(
        PersistenceErrorCode.NOT_FOUND,
        "Entity (%s) not found with id (%d) in the persistence layer"
            .formatted(model.getClass().getSimpleName(), model.getId()),
        null);
  }

  public static PersistenceException notFound(
      Class<? extends BaseIdentifiableModel<Long>> modelClazz, Long id) {
    return new PersistenceException(
        PersistenceErrorCode.NOT_FOUND,
        "Entity (%s) not found with id (%d) in the persistence layer"
            .formatted(modelClazz.getSimpleName(), id),
        null);
  }

  public static PersistenceException notPersisted(BaseIdentifiableModel<?> model) {
    return new PersistenceException(
        PersistenceErrorCode.NOT_PERSISTED,
        "Entity (%s) is not persisted yet.".formatted(model.getClass().getSimpleName()),
        null);
  }

  public static PersistenceException alreadyPersisted(BaseIdentifiableModel<?> model) {
    return new PersistenceException(
        PersistenceErrorCode.ALREADY_PERSISTED,
        "Entity (%s) is already persisted.".formatted(model.getClass().getSimpleName()),
        null);
  }
}
