package com.automation.ui.dataproviders;

public class LoginTestData {

  public final String testCaseName;
  public final String username;
  public final String password;
  public final boolean expectedSuccess;
  public final String expectedErrorSnippet;

  public LoginTestData(
    String testCaseName,
    String username,
    String password,
    boolean expectedSuccess,
    String expectedErrorSnippet
  ) {
    this.testCaseName = testCaseName;
    this.username = username;
    this.password = password;
    this.expectedSuccess = expectedSuccess;
    this.expectedErrorSnippet = expectedErrorSnippet;
  }

  @Override
  public String toString() {
    return testCaseName;
  }
}
