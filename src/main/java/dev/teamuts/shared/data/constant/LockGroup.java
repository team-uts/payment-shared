package dev.teamuts.shared.data.constant;

import org.jspecify.annotations.NonNull;

public interface LockGroup extends KtLockGroup {
  String name();

  @Override
  default @NonNull String getName() {
    return name();
  }
}
