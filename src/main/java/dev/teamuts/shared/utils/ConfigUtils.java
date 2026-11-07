package dev.teamuts.shared.utils;

import java.util.Properties;

public class ConfigUtils {
  public static Properties mergeProperties(Properties first, Properties second) {
    return new Properties() {
      {
        this.putAll(first);
        this.putAll(second);
      }
    };
  }
}
