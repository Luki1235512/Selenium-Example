package com.automation.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class ExtentManager {

  private static final Object LOCK = new Object();
  private static volatile ExtentReports extent;

  public static ExtentReports getInstance() {
    if (extent == null) {
      synchronized (LOCK) {
        if (extent == null) {
          ExtentSparkReporter spark = new ExtentSparkReporter(
            "target/extent-report.html"
          );
          spark.config().setDocumentTitle("Automation Test Report");
          spark.config().setReportName("Selenium-Example Test Results");

          ExtentReports instance = new ExtentReports();
          instance.attachReporter(spark);
          extent = instance;
        }
      }
    }
    return extent;
  }
}
