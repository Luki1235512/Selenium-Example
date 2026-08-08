package com.automation.listeners;

import com.automation.utils.ExtentManager;
import com.automation.utils.ExtentTestManager;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class ExtentTestNameListener implements ITestListener {

  @Override
  public void onTestStart(ITestResult result) {
    ExtentTest test = ExtentManager.getInstance().createTest(
      buildTestName(result)
    );
    ExtentTestManager.setTest(test);
  }

  @Override
  public void onTestSuccess(ITestResult result) {
    Integer retryAttempt = (Integer) result.getAttribute("retryAttempt");
    if (retryAttempt != null && retryAttempt > 0) {
      ExtentTestManager.getTest().log(
        Status.WARNING,
        "Test passed after " +
          retryAttempt +
          " retry attempt(s) — flagged as flaky"
      );
    } else {
      ExtentTestManager.getTest().log(Status.PASS, "Test passed");
    }
  }

  @Override
  public void onTestFailure(ITestResult result) {
    ExtentTestManager.getTest().log(
      Status.FAIL,
      "Test failed: " + result.getThrowable()
    );
  }

  @Override
  public void onTestSkipped(ITestResult result) {
    ExtentTestManager.getTest().log(Status.SKIP, "Test skipped");
  }

  private String buildTestName(ITestResult result) {
    String baseName = result.getMethod().getMethodName();
    Object[] params = result.getParameters();

    if (params.length == 0) {
      return baseName;
    }

    StringBuilder name = new StringBuilder(baseName);
    for (Object param : params) {
      name.append("_").append(param);
    }
    return name.toString();
  }
}
