package dev.teamuts.shared.data.lock;

@FunctionalInterface
public interface LockReleaseCallback {
  void execute();
}
