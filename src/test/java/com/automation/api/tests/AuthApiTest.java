package com.automation.api.tests;

import static io.restassured.RestAssured.given;

import com.automation.api.assertions.ApiAssertions;
import com.automation.api.base.BaseApiTest;
import com.automation.api.models.LoginResponse;
import com.automation.api.models.RegisterResponse;
import com.automation.utils.ConfigReader;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import org.testng.Assert;
import org.testng.annotations.Test;

public class AuthApiTest extends BaseApiTest {

  @Test
  public void login_withMissingPassword_returns400() {
    Map<String, Object> payload = new HashMap<>();
    payload.put("email", ConfigReader.get("demo.email"));

    Response response = given()
      .spec(apiSpec)
      .body(payload)
      .when()
      .post("/api/login")
      .then()
      .extract()
      .response();

    ApiAssertions.assertStatusCode(response, 400);
    ApiAssertions.assertFieldPresent(response, "error");
  }

  @Test
  public void login_withValidCredentials_returnsToken() {
    Map<String, Object> payload = new HashMap<>();
    payload.put("email", ConfigReader.get("demo.email"));
    payload.put("password", ConfigReader.get("demo.password"));

    Response response = given()
      .spec(apiSpec)
      .body(payload)
      .when()
      .post("/api/login")
      .then()
      .extract()
      .response();

    ApiAssertions.assertStatusCode(response, 200);

    LoginResponse body = response.as(LoginResponse.class);
    Assert.assertNotNull(
      body.getToken(),
      "Successful login should return a token"
    );
  }

  @Test
  public void register_withValidCredentials_returnsToken() {
    Map<String, Object> payload = new HashMap<>();
    payload.put("email", ConfigReader.get("demo.email"));
    payload.put("password", ConfigReader.get("demo.password"));

    Response response = given()
      .spec(apiSpec)
      .body(payload)
      .when()
      .post("/api/register")
      .then()
      .extract()
      .response();

    ApiAssertions.assertStatusCode(response, 200);

    RegisterResponse body = response.as(RegisterResponse.class);
    Assert.assertNotNull(
      body.getToken(),
      "Successful registration should return a token"
    );
  }
}
