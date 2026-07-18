package com.automation.base;

import com.automation.utils.ConfigReader;
import com.automation.utils.ExtentManager;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
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
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;

public class BaseTest {

  private static final Logger logger = LogManager.getLogger(BaseTest.class);

  protected WebDriver driver;
  protected static ExtentReports extent = ExtentManager.getInstance();
  protected ExtentTest test;

  @BeforeMethod
  public void setUp(java.lang.reflect.Method method) {
    driver = new ChromeDriver();
    driver.get(ConfigReader.get("base.url"));
    test = extent.createTest(method.getName());
    logger.info("Starting test: {}", method.getName());
  }

  @AfterMethod
  public void tearDown(ITestResult result) {
    if (result.getStatus() == ITestResult.FAILURE) {
      test.log(Status.FAIL, "Test failed: " + result.getThrowable());
      String screenshotPath = captureScreenshot(result.getName());
      test.addScreenCaptureFromPath(screenshotPath);
      logger.error("Test failed: {}", result.getName());
    } else if (result.getStatus() == ITestResult.SUCCESS) {
      test.log(Status.PASS, "Test passed");
      logger.info("Test passed: {}", result.getName());
    }

    if (driver != null) {
      driver.quit();
    }
  }

  @AfterSuite
  public void tearDownSuite() {
    extent.flush();
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
