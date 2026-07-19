package com.automation.api.client;

import com.automation.utils.ExtentTestManager;
import com.aventstack.extentreports.ExtentTest;
import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ExtentLoggingFilter implements Filter {

  private static final Logger logger = LogManager.getLogger(
    ExtentLoggingFilter.class
  );

  @Override
  public Response filter(
    FilterableRequestSpecification requestSpec,
    FilterableResponseSpecification responseSpec,
    FilterContext context
  ) {
    long start = System.currentTimeMillis();
    Response response = context.next(requestSpec, responseSpec);
    long durationMs = System.currentTimeMillis() - start;

    String summary = String.format(
      "%s %s -> %d (%dms)",
      requestSpec.getMethod(),
      requestSpec.getURI(),
      response.getStatusCode(),
      durationMs
    );
    logger.info(summary);

    ExtentTest test = ExtentTestManager.getTest();
    if (test != null) {
      test.info(summary);
    }

    return response;
  }
}
