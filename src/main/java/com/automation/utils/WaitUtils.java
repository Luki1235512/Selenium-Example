package com.automation.utils;

import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class WaitUtils {

  private static final int DEFAULT_TIMEOUT_SECONDS = 10;

  private WebDriverWait wait;

  public WaitUtils(WebDriver driver) {
    this.wait = new WebDriverWait(
      driver,
      Duration.ofSeconds(DEFAULT_TIMEOUT_SECONDS)
    );
  }

  public WebElement waitForVisible(By locator) {
    return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
  }

  public WebElement waitForClickable(By locator) {
    return wait.until(ExpectedConditions.elementToBeClickable(locator));
  }
}
