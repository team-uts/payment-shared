package dev.teamuts.shared.data.lock;

import java.util.Optional;

public interface CacheLockPort {
  /**
   * Acquire a lock for the given key.
   *
   * @param key The key to lock
   * @param ttlInMillis Time to live in milliseconds
   */
  Optional<LockReleaseCallback> lock(String key, long ttlInMillis);
}
