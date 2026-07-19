package com.automation.base;

import com.automation.utils.ConfigReader;
import com.automation.utils.ExtentTestManager;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public class BaseTest {

  private static final Logger logger = LogManager.getLogger(BaseTest.class);
  protected WebDriver driver;

  @BeforeMethod
  public void setUp(java.lang.reflect.Method method) {
    driver = new ChromeDriver();
    driver.get(ConfigReader.get("base.url"));
    logger.info("Browser launched, navigated to base URL");
  }

  @AfterMethod
  public void tearDown(ITestResult result) {
    if (result.getStatus() == ITestResult.FAILURE) {
      String screenshotPath = captureScreenshot(result.getName());
      if (screenshotPath != null) {
        ExtentTestManager.getTest().addScreenCaptureFromPath(screenshotPath);
      }
      logger.error("Test failed: {}", result.getName());
    }

    if (driver != null) {
      driver.quit();
    }

    ExtentTestManager.unload();
  }

  private String captureScreenshot(String testName) {
    try {
      TakesScreenshot ts = (TakesScreenshot) driver;
      File source = ts.getScreenshotAs(OutputType.FILE);
      String relativePath = "screenshots/" + testName + ".png";
      Files.createDirectories(Paths.get("target/screenshots"));
      Files.copy(source.toPath(), Paths.get("target/" + relativePath));
      return relativePath;
    } catch (Exception e) {
      logger.error("Failed to capture screenshot: {}", e.getMessage());
      return null;
    }
  }
}
