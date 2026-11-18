package dev.teamuts.shared.data.lock;

import dev.teamuts.shared.data.PersistenceErrorCode;
import dev.teamuts.shared.data.PersistenceException;
import dev.teamuts.shared.data.annotation.RequireEntityLock;
import dev.teamuts.shared.domain.model.BaseIdentifiableModel;
import jakarta.persistence.EntityManager;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

@Aspect
public class RequireEntityLockAspect {
  private final EntityLockChecker entityLockChecker;
  private final EntityManager entityManager;

  public RequireEntityLockAspect(EntityLockChecker entityLockChecker, EntityManager entityManager) {
    this.entityLockChecker = Objects.requireNonNull(entityLockChecker);
    this.entityManager = Objects.requireNonNull(entityManager);
  }

  @Around("@annotation(dev.teamuts.shared.data.annotation.RequireEntityLock)")
  public Object checkEntityLock(ProceedingJoinPoint joinPoint) throws Throwable {
    RequireEntityLock annotation =
        ((MethodSignature) joinPoint).getMethod().getAnnotation(RequireEntityLock.class);

    Class<?> entityClass = annotation.entity();

    checkValidEntity(entityClass);

    BaseIdentifiableModel<?> model = getModel(joinPoint.getArgs());

    entityLockChecker.requireLocked(model, entityClass);

    return joinPoint.proceed();
  }

  private void checkValidEntity(Class<?> target) {
    try {
      entityManager.getMetamodel().entity(target);
    } catch (Exception e) {
      throw PersistenceException.of(
          PersistenceErrorCode.ENTITY_LOCK_CHECKING_FAILED,
          "The specified entity class is not a valid JPA entity: %s".formatted(target.getName()));
    }
  }

  /**
   * The method must declare exactly one parameter whose type implements<br>
   * {@link BaseIdentifiableModel}
   */
  private BaseIdentifiableModel<?> getModel(Object[] args) {
    List<? extends BaseIdentifiableModel<?>> models =
        Arrays.stream(args)
            .filter(BaseIdentifiableModel.class::isInstance)
            .map(arg -> (BaseIdentifiableModel<?>) arg)
            .toList();

    if (models.size() != 1) {
      throw PersistenceException.of(
          PersistenceErrorCode.ENTITY_LOCK_CHECKING_FAILED,
          "Exactly one argument of type BaseIdentifiableModel is required for entity lock checking.");
    }

    return models.getFirst();
  }
}
