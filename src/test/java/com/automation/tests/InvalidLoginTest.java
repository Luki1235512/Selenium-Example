package com.automation.tests;

import com.automation.base.BaseTest;
import com.automation.pages.LoginPage;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;

public class InvalidLoginTest extends BaseTest {

  @Test
  public void invalidLoginTest() {
    LoginPage loginPage = new LoginPage(driver);
    loginPage.login("locked_out_user", "secret_sauce");

    String errorMessage = driver
      .findElement(By.cssSelector("[data-test='error']"))
      .getText();
    Assert.assertTrue(errorMessage.contains("locked out"));
  }
}
