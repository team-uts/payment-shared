package dev.teamuts.shared.data.lock;

import java.util.List;

public interface LockGroup {
  String generateLockKey(List<String> params);
}
