package com.automation.listeners;

import com.automation.utils.ExtentManager;
import org.testng.ISuite;
import org.testng.ISuiteListener;

public class ExtentFlushListener implements ISuiteListener {

  @Override
  public void onFinish(ISuite suite) {
    ExtentManager.getInstance().flush();
  }
}
