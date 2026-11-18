package dev.teamuts.shared.data.annotation;

import dev.teamuts.shared.data.constant.LockKeyType;

public @interface LockKeyPath {
  LockKeyType keyType() default LockKeyType.OBJECT;

  LockKeyParam[] keyParams() default {};

  @interface LockKeyParam {
    String key();

    String field() default "";
  }
}
