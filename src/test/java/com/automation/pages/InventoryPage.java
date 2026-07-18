package com.automation.pages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class InventoryPage {

  private static final Logger logger = LogManager.getLogger(
    InventoryPage.class
  );

  private WebDriver driver;
  private By pageTitle = By.className("title");

  public InventoryPage(WebDriver driver) {
    this.driver = driver;
  }

  public String getPageTitleExists() {
    String title = driver.findElement(pageTitle).getText();
    logger.info("Inventory page title read as: {}", title);
    return title;
  }

  public String getCurrentUrl() {
    String url = driver.getCurrentUrl();
    logger.info("Current URL: {}", url);
    return url;
  }
}
