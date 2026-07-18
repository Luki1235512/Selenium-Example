package com.automation.tests;

import com.automation.base.BaseTest;
import com.automation.dataproviders.LoginDataProvider;
import com.automation.dataproviders.LoginTestData;
import com.automation.pages.InventoryPage;
import com.automation.pages.LoginPage;
import com.automation.utils.ConfigReader;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LoginDataDrivenTest extends BaseTest {

  @Test(
    dataProvider = "loginCredentials",
    dataProviderClass = LoginDataProvider.class
  )
  public void loginTest(LoginTestData data) {
    LoginPage loginPage = new LoginPage(driver);
    loginPage.login(data.username, data.password);

    if (data.expectedSuccess) {
      InventoryPage inventoryPage = new InventoryPage(driver);
      Assert.assertEquals(
        inventoryPage.getCurrentUrl(),
        ConfigReader.get("base.url") + "/inventory.html"
      );
    } else {
      String errorMessage = loginPage.getErrorMessage();
      Assert.assertTrue(
        errorMessage.contains(data.expectedErrorSnippet),
        "Expected error to containt '" +
          data.expectedErrorSnippet +
          "' but was: " +
          errorMessage
      );
    }
  }
}
