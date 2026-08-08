package com.automation.api.client;

import com.automation.utils.ConfigReader;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class ApiClient {

  private static final Object LOCK = new Object();
  private static volatile RequestSpecification requestSpec;

  public static RequestSpecification getSpec() {
    if (requestSpec == null) {
      synchronized (LOCK) {
        if (requestSpec == null) {
          requestSpec = new RequestSpecBuilder()
            .setBaseUri(ConfigReader.get("api.base.url"))
            .addHeader("x-api-key", ConfigReader.getSecret("REQRES_API_KEY"))
            .setContentType(ContentType.JSON)
            .addFilter(new ExtentLoggingFilter())
            .build();
        }
      }
    }
    return requestSpec;
  }
}
