package com.automation.pages;

import com.automation.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage {

  private WaitUtils wait;

  private By usernameField = By.id("user-name");
  private By passwordField = By.id("password");
  private By loginButton = By.id("login-button");

  public LoginPage(WebDriver driver) {
    this.wait = new WaitUtils(driver);
  }

  public void enterUsername(String username) {
    wait.waitForVisible(usernameField).sendKeys(username);
  }

  public void enterPassword(String password) {
    wait.waitForVisible(passwordField).sendKeys(password);
  }

  public void clickLogin() {
    wait.waitForClickable(loginButton).click();
  }

  public void login(String username, String password) {
    enterUsername(username);
    enterPassword(password);
    clickLogin();
  }
}
