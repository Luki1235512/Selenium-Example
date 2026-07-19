package com.automation.api.base;

import com.automation.api.client.ApiClient;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;

public class BaseApiTest {

  protected RequestSpecification apiSpec;

  @BeforeClass
  public void setUpApiClient() {
    apiSpec = ApiClient.getSpec();
  }
}
