package com.automation.tests;

import com.automation.base.BaseTest;
import com.automation.pages.InventoryPage;
import com.automation.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LoginTest extends BaseTest {

  @Test
  public void validLoginTest() {
    LoginPage loginPage = new LoginPage(driver);
    loginPage.login("standard_user", "secret_sauce");

    InventoryPage inventoryPage = new InventoryPage(driver);

    Assert.assertEquals(
      inventoryPage.getCurrentUrl(),
      "https://www.saucedemo.com/inventory.html"
    );
    Assert.assertEquals(inventoryPage.getPageTitleExists(), "Products");
  }
}
