package com.automation.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {

  private static Properties properties;

  static {
    try {
      String path = "src/test/resources/config.properties";
      FileInputStream fis = new FileInputStream(path);
      properties = new Properties();
      properties.load(fis);
    } catch (IOException e) {
      throw new RuntimeException(
        "Failed to load config.properties: " + e.getMessage()
      );
    }
  }

  public static String get(String key) {
    String value = properties.getProperty(key);
    if (value == null) {
      throw new RuntimeException(
        "Property not found in config.properties: " + key
      );
    }
    return value;
  }
}
