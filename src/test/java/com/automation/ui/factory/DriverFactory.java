package com.automation.ui.factory;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

public class DriverFactory {

  private DriverFactory() {}

  public static WebDriver createDriver(String browser, boolean headless) {
    return switch (browser.toLowerCase()) {
      case "chrome" -> createChromeDriver(headless);
      case "firefox" -> createFirefoxDriver(headless);
      default -> throw new IllegalArgumentException(
        "Unsupported browser: " + browser
      );
    };
  }

  private static WebDriver createChromeDriver(boolean headless) {
    ChromeOptions options = new ChromeOptions();
    if (headless) {
      options.addArguments("--headless=new");
      options.addArguments("--no-sandbox");
      options.addArguments("--disable-dev-shm-usage");
    }
    return new ChromeDriver(options);
  }

  private static WebDriver createFirefoxDriver(boolean headless) {
    FirefoxOptions options = new FirefoxOptions();
    if (headless) {
      options.addArguments("-headless");
    }
    return new FirefoxDriver(options);
  }
}
