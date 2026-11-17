package dev.teamuts.shared.data.lock;

public interface CacheLockPort {
  /**
   * Acquire a lock for the given key.
   *
   * @param key The key to lock
   * @param ttlInMillis Time to live in milliseconds
   */
  boolean lock(String key, long ttlInMillis);

  /**
   * Release the lock for the given key.
   *
   * @param key The key to unlock
   */
  void unlock(String key);
}
