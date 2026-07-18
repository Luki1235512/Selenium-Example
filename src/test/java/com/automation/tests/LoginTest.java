package com.automation.tests;

import com.automation.pages.InventoryPage;
import com.automation.pages.LoginPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class LoginTest {

  private WebDriver driver;

  @BeforeMethod
  public void setUp() {
    driver = new ChromeDriver();
    driver.get("https://www.saucedemo.com");
  }

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

  @AfterMethod
  public void tearDown() {
    driver.quit();
  }
}
