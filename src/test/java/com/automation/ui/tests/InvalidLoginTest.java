package com.automation.ui.tests;

import com.automation.ui.base.BaseTest;
import com.automation.ui.pages.LoginPage;
import com.automation.utils.ConfigReader;
import org.testng.Assert;
import org.testng.annotations.Test;

public class InvalidLoginTest extends BaseTest {

  @Test
  public void login_withLockedOutUser_showsLockedOutError() {
    LoginPage loginPage = new LoginPage(getDriver());
    loginPage.login(
      ConfigReader.get("locked.username"),
      ConfigReader.get("standard.password")
    );

    String errorMessage = loginPage.getErrorMessage();
    Assert.assertTrue(errorMessage.contains("locked out"));
  }
}
