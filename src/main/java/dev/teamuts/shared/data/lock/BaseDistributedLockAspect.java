package dev.teamuts.shared.data.lock;

import dev.teamuts.shared.data.PersistenceErrorCode;
import dev.teamuts.shared.data.PersistenceException;
import dev.teamuts.shared.data.annotation.LockKeyPath;
import dev.teamuts.shared.data.annotation.TryLock;
import dev.teamuts.shared.data.annotation.WaitLock;
import dev.teamuts.shared.data.constant.KtLockGroup;
import dev.teamuts.shared.data.constant.LockGroup;
import dev.teamuts.shared.data.constant.LockKeyType;
import dev.teamuts.shared.data.constant.LockTtl;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

/**
 * Base aspect for distributed lock handling.<br>
 * Each project should implement this base aspect class and provide the {@link CacheLockPort}
 * implementation.<br>
 * I leave {@link KtLockGroup} and {@link LockGroup} interface here to implement the lock group enum
 * class in each project, because, each project has its own domain group.
 */
@Slf4j
public abstract class BaseDistributedLockAspect {
  private final CacheLockPort cacheLockPort;

  private static final char LOCK_KEY_SEPARATOR = ':';

  protected BaseDistributedLockAspect(CacheLockPort cacheLockPort) {
    this.cacheLockPort = cacheLockPort;
  }

  public Object acquireLock(ProceedingJoinPoint joinPoint) throws Throwable {
    Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();

    LockAnnotationSetup lockSetup = getLockAnnotationSetup(method);
    KtLockGroup lockGroup = getLockGroup(lockSetup.group());

    String key = generateLockKey(method, lockGroup, lockSetup.keyPaths(), joinPoint.getArgs());

    // if the lock TTL is not specified in the annotation, use the default TTL from the lock group
    LockTtl ttl = lockSetup.ttl().isZero() ? lockGroup.getLockTtl() : lockSetup.ttl();
    // acquire lock
    boolean isLocked = cacheLockPort.lock(key, ttl.millis());

    if (!isLocked) {
      throw PersistenceException.of(
          PersistenceErrorCode.LOCK_ACQUISITION_FAILED,
          "Failed to acquire lock for key: %s".formatted(key));
    }

    String className = joinPoint.getTarget().getClass().getSimpleName();

    try {
      log.debug("[{}] Lock Acquired >> {}", className, key);
      return joinPoint.proceed();
    } finally {
      // release lock
      cacheLockPort.unlock(key);
      log.debug("[{}] Lock Released >> {}", className, key);
    }
  }

  private LockAnnotationSetup getLockAnnotationSetup(Method method) {
    TryLock tryLock = method.getAnnotation(TryLock.class);
    if (tryLock != null) {
      return new LockAnnotationSetup(tryLock.group(), tryLock.keyPaths(), tryLock.timeToLive());
    }

    WaitLock waitLock = method.getAnnotation(WaitLock.class);
    if (waitLock != null) {
      return new LockAnnotationSetup(waitLock.group(), waitLock.keyPaths(), waitLock.timeToLive());
    }

    throw PersistenceException.of(
        PersistenceErrorCode.INVALID_LOCK_ANNOTATION,
        "No lock annotation found on method: %s".formatted(method.getName()));
  }

  /**
   * implement this method.
   *
   * @return {@link KtLockGroup} or {@link LockGroup} implemented enum type.
   */
  protected abstract KtLockGroup getLockGroup(String groupName);

  private String generateLockKey(
      Method method, KtLockGroup group, LockKeyPath[] keyPaths, Object[] args) {
    // get param index for each key param annotation setup
    // LockKeyPath to (LockKeyType, index of param)
    Map<LockKeyPath.LockKeyParam, KeyParamBinding> paramIndexMap =
        Arrays.stream(keyPaths)
            .flatMap(keyPath ->
                Arrays.stream(keyPath.keyParams())
                    .map(keyParam -> {
                      Parameter[] params = method.getParameters();

                      int indexOfParam =
                          IntStream.range(0, params.length)
                              .filter(i -> keyParam.key().equals(params[i].getName()))
                              .findFirst()
                              .orElseThrow(() ->
                                  PersistenceException.of(
                                      PersistenceErrorCode.INVALID_LOCK_ANNOTATION,
                                      "No parameter found for key: " + keyParam.key()));

                      return Map.entry(
                          keyParam, new KeyParamBinding(keyPath.keyType(), indexOfParam));
                    }))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

    // get the values of params.
    String argValues =
        paramIndexMap.entrySet().stream()
            .map(entry -> {
              LockKeyPath.LockKeyParam keyParam = entry.getKey();
              KeyParamBinding binding = entry.getValue();
              Object arg = args[binding.paramIndex()];

              return switch (binding.keyType()) {
                case LockKeyType.OBJECT -> readField(arg, keyParam.field());
                case LockKeyType.NON_OBJECT -> arg.toString();
              };
            })
            .collect(Collectors.joining(String.valueOf(LOCK_KEY_SEPARATOR)));

    // generate lock key
    // LOCK:<group_name>:<arg1>:<arg2>:...
    return "LOCK%c%s%c%s"
        .formatted(LOCK_KEY_SEPARATOR, group.getName(), LOCK_KEY_SEPARATOR, argValues);
  }

  private String readField(Object arg, String fieldName) {
    try {
      Field field = arg.getClass().getDeclaredField(fieldName);
      field.setAccessible(true);
      return field.get(arg).toString();
    } catch (NoSuchFieldException | IllegalAccessException e) {
      throw new IllegalStateException(
          "Cannot read lock key field '" + fieldName + "' from " + arg.getClass().getName(), e);
    }
  }

  private record KeyParamBinding(LockKeyType keyType, int paramIndex) {}

  private record LockAnnotationSetup(String group, LockKeyPath[] keyPaths, LockTtl ttl) {}
}
