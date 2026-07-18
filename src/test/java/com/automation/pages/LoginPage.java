package com.automation.pages;

import com.automation.utils.WaitUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage {

  private static final Logger logger = LogManager.getLogger(LoginPage.class);

  private WaitUtils wait;

  private By usernameField = By.id("user-name");
  private By passwordField = By.id("password");
  private By loginButton = By.id("login-button");

  public LoginPage(WebDriver driver) {
    this.wait = new WaitUtils(driver);
  }

  public void enterUsername(String username) {
    logger.info("Entering username: {}", username);
    wait.waitForVisible(usernameField).sendKeys(username);
  }

  public void enterPassword(String password) {
    logger.info("Entering password");
    wait.waitForVisible(passwordField).sendKeys(password);
  }

  public void clickLogin() {
    logger.info("Clicking login button");
    wait.waitForClickable(loginButton).click();
  }

  public void login(String username, String password) {
    logger.info("Attempting login flow");
    enterUsername(username);
    enterPassword(password);
    clickLogin();
  }
}
