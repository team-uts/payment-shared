package dev.teamuts.shared.utils;

import java.nio.charset.StandardCharsets;

public class StringUtils {
  public static String truncate(String str, int maxLength) {
    if (str == null) {
      return null;
    }

    String target = str.trim();

    if (target.isEmpty()) {
      return target;
    }

    if (target.length() <= maxLength) {
      return target;
    }

    return target.substring(0, maxLength);
  }

  public static String covertByteArrayToStr(byte[] bytes, int maxLength) {
    return new String(bytes, 0, Math.min(bytes.length, maxLength), StandardCharsets.UTF_8);
  }
}
