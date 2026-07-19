package com.automation.api.tests;

import static io.restassured.RestAssured.given;

import com.automation.api.assertions.ApiAssertions;
import com.automation.api.base.BaseApiTest;
import com.automation.api.models.LegacyMutationResponse;
import com.automation.api.models.LegacyUserListResponse;
import com.automation.api.models.LegacyUserResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import org.testng.Assert;
import org.testng.annotations.Test;

public class UserApiTest extends BaseApiTest {

  @Test
  public void getUserList_returnsPaginatedUsers() {
    Response response = given()
      .spec(apiSpec)
      .queryParams("page", 1)
      .when()
      .get("/api/users")
      .then()
      .extract()
      .response();

    ApiAssertions.assertStatusCode(response, 200);
    ApiAssertions.assertResponseTimeUnder(response, 3000);

    LegacyUserListResponse body = response.as(LegacyUserListResponse.class);
    Assert.assertFalse(
      body.getData().isEmpty(),
      "Expected at least one user in the list"
    );
    Assert.assertEquals(body.getPage(), 1);
  }

  @Test
  public void getUserById_returnsSingleUser() {
    Response response = given()
      .spec(apiSpec)
      .when()
      .get("/api/users/{id}", 2)
      .then()
      .extract()
      .response();

    ApiAssertions.assertStatusCode(response, 200);

    LegacyUserResponse body = response.as(LegacyUserResponse.class);
    Assert.assertEquals(body.getData().getId(), 2);
    Assert.assertNotNull(body.getData().getEmail());
  }

  @Test
  public void getUserById_notFound_return404() {
    Response response = given()
      .spec(apiSpec)
      .when()
      .get("/api/users/{id}", 999999)
      .then()
      .extract()
      .response();

    ApiAssertions.assertStatusCode(response, 404);
  }

  @Test
  public void createUser_returnsCreatedResourceWithId() {
    Map<String, Object> payload = new HashMap<>();
    payload.put("name", "morpheus");
    payload.put("job", "leader");

    Response response = given()
      .spec(apiSpec)
      .body(payload)
      .when()
      .post("/api/users")
      .then()
      .extract()
      .response();

    ApiAssertions.assertStatusCode(response, 201);

    LegacyMutationResponse body = response.as(LegacyMutationResponse.class);
    Assert.assertNotNull(body.getId(), "Created user should be assigned an id");
    Assert.assertNotNull(
      body.getCreatedAt(),
      "Created user should have a createdAt timestamp"
    );
  }

  @Test
  public void updateUser_returnsUpdatedTimestamp() {
    Map<String, Object> payload = new HashMap<>();
    payload.put("name", "morpheus");
    payload.put("job", "zion resident");

    Response response = given()
      .spec(apiSpec)
      .body(payload)
      .when()
      .put("/api/users/{id}", 2)
      .then()
      .extract()
      .response();

    ApiAssertions.assertStatusCode(response, 200);

    LegacyMutationResponse body = response.as(LegacyMutationResponse.class);
    Assert.assertNotNull(
      body.getUpdatedAt(),
      "Updated user should have an updatedAt timestamp"
    );
  }

  @Test
  public void deleteUser_returnsNoContent() {
    given()
      .spec(apiSpec)
      .when()
      .delete("/api/users/{id}", 2)
      .then()
      .statusCode(204);
  }
}
