package com.automation.api.tests;

import static io.restassured.RestAssured.given;

import com.automation.api.assertions.ApiAssertions;
import com.automation.api.base.BaseApiTest;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import org.testng.annotations.Test;

public class AuthApiTest extends BaseApiTest {

  @Test
  public void login_withMissingPassword_returns400() {
    Map<String, Object> payload = new HashMap<>();
    payload.put("email", "eve.holt@reqres.in");

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
}
