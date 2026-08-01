package com.automation.ui.tests;

import com.automation.testgroups.TestGroups;
import com.automation.ui.base.BaseTest;
import com.automation.ui.pages.InventoryPage;
import com.automation.ui.pages.LoginPage;
import com.automation.utils.ConfigReader;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LoginTest extends BaseTest {

  @Test(groups = { TestGroups.SMOKE, TestGroups.UI })
  public void login_withValidCredentials_navigatesToInventory() {
    LoginPage loginPage = new LoginPage(getDriver());
    loginPage.login(
      ConfigReader.get("standard.username"),
      ConfigReader.get("standard.password")
    );

    InventoryPage inventoryPage = new InventoryPage(getDriver());

    Assert.assertEquals(
      inventoryPage.getCurrentUrl(),
      ConfigReader.get("base.url") + "/inventory.html"
    );
    Assert.assertEquals(inventoryPage.getPageTitleExists(), "Products");
  }
}
