package com.automation.tests;

import com.automation.base.BaseTest;
import com.automation.pages.LoginPage;
import com.automation.utils.ConfigReader;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;

public class InvalidLoginTest extends BaseTest {

  @Test
  public void invalidLoginTest() {
    LoginPage loginPage = new LoginPage(driver);
    loginPage.login(
      ConfigReader.get("locked.username"),
      ConfigReader.get("standard.password")
    );

    String errorMessage = driver
      .findElement(By.cssSelector("[data-test='error']"))
      .getText();
    Assert.assertTrue(errorMessage.contains("locked out"));
  }
}
