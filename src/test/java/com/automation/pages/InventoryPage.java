package com.automation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class InventoryPage {

  private WebDriver driver;
  private By pageTitle = By.className("title");

  public InventoryPage(WebDriver driver) {
    this.driver = driver;
  }

  public String getPageTitleExists() {
    return driver.findElement(pageTitle).getText();
  }

  public String getCurrentUrl() {
    return driver.getCurrentUrl();
  }
}
