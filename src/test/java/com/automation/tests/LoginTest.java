package com.automation.tests;

import com.automation.base.BaseTest;
import com.automation.pages.InventoryPage;
import com.automation.pages.LoginPage;
import com.automation.utils.ConfigReader;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LoginTest extends BaseTest {

  @Test
  public void validLoginTest() {
    LoginPage loginPage = new LoginPage(driver);
    loginPage.login(
      ConfigReader.get("standard.username"),
      ConfigReader.get("standard.password")
    );

    InventoryPage inventoryPage = new InventoryPage(driver);

    Assert.assertEquals(
      inventoryPage.getCurrentUrl(),
      ConfigReader.get("base.url") + "/inventory.html"
    );
    Assert.assertEquals(inventoryPage.getPageTitleExists(), "Products");
  }
}
