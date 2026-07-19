package com.automation.api.assertions;

import io.restassured.response.Response;
import org.testng.Assert;

public class ApiAssertions {

  private ApiAssertions() {}

  public static void assertStatusCode(Response response, int expected) {
    Assert.assertEquals(
      response.getStatusCode(),
      expected,
      "Unexpected status code. Body: " + response.getBody().asPrettyString()
    );
  }

  public static void assertResponseTimeUnder(
    Response response,
    long maxMillis
  ) {
    long actual = response.getTime();
    Assert.assertTrue(
      actual <= maxMillis,
      "Response time " + actual + "ms exceeded limit of " + maxMillis + "ms"
    );
  }

  public static void assertFieldPresent(Response response, String jsonPath) {
    Object actual = response.jsonPath().get(jsonPath);
    Assert.assertNotNull(
      actual,
      "Expected field to be present at JSON path: " + jsonPath
    );
  }
}
