package dev.teamuts.shared.data;

import dev.teamuts.shared.domain.model.BaseIdentifiableModel;
import jakarta.persistence.EntityManager;
import java.util.Objects;
import org.springframework.stereotype.Component;

/** Checks pessimistic locks known to the current JPA persistence context. */
@Component
public class EntityLockChecker {
  private final EntityManager entityManager;

  public EntityLockChecker(EntityManager entityManager) {
    this.entityManager = Objects.requireNonNull(entityManager);
  }

  /**
   * Returns whether the current transaction holds a JPA pessimistic lock on the model's row.
   *
   * <p>The entity class must map the supplied model, using the same primary key. This method may
   * load the entity, but does not request a lock. It cannot detect locks acquired through native
   * SQL, implicit database locks, or locks held by another transaction. A false result means JPA
   * does not report a pessimistic lock; it does not prove the database row is unlocked.
   */
  public boolean isLocked(BaseIdentifiableModel<?> model, Class<?> entityClass) {
    Objects.requireNonNull(model);
    Objects.requireNonNull(entityClass);

    if (!entityManager.isJoinedToTransaction()) {
      return false;
    }

    model.checkPersisted();
    Object entity = entityManager.find(entityClass, model.getId());
    if (entity == null) {
      return false;
    }

    return switch (entityManager.getLockMode(entity)) {
      case PESSIMISTIC_READ, PESSIMISTIC_WRITE, PESSIMISTIC_FORCE_INCREMENT -> true;
      default -> false;
    };
  }

  /** Requires a pessimistic lock reported by JPA. */
  public void requireLocked(BaseIdentifiableModel<?> model, Class<?> entityClass) {
    if (!isLocked(model, entityClass)) {
      throw PersistenceException.notAcquiredLock(model, entityClass);
    }
  }
}
