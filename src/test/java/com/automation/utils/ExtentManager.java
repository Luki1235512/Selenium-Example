package com.automation.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class ExtentManager {

  private static ExtentReports extent;

  public static ExtentReports getInstance() {
    if (extent == null) {
      ExtentSparkReporter spark = new ExtentSparkReporter(
        "target/extent-report.html"
      );
      spark.config().setDocumentTitle("Automation Test Report");
      spark.config().setReportName("Selenium-Example Test Results");

      extent = new ExtentReports();
      extent.attachReporter(spark);
    }
    return extent;
  }
}
