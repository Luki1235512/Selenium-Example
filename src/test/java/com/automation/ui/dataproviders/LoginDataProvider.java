package com.automation.ui.dataproviders;

import org.testng.annotations.DataProvider;

public class LoginDataProvider {

  @DataProvider(name = "loginCredentials")
  public static Object[][] loginCredentials() {
    return new Object[][] {
      {
        new LoginTestData(
          "valid_login",
          "standard_user",
          "secret_sauce",
          true,
          null
        ),
      },
      {
        new LoginTestData(
          "locked_out_user",
          "locked_out_user",
          "secret_sauce",
          false,
          "locked out"
        ),
      },
      {
        new LoginTestData(
          "wrong_password",
          "standard_user",
          "wrong_password",
          false,
          "do not match"
        ),
      },
      {
        new LoginTestData(
          "empty_fields",
          "",
          "",
          false,
          "Username is required"
        ),
      },
    };
  }
}
