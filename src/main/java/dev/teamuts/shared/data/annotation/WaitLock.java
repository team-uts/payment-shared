package dev.teamuts.shared.data.annotation;

import dev.teamuts.shared.data.constant.LockTtl;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface WaitLock {
  String group();

  LockKeyPath[] keyPaths() default {};

  LockTtl timeToLive() default LockTtl.ZERO;
}
