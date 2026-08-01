package com.automation.listeners;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import org.testng.IAnnotationTransformer;
import org.testng.annotations.ITestAnnotation;
import org.testng.internal.annotations.DisabledRetryAnalyzer;

public class RetryAnalyzerTransformer implements IAnnotationTransformer {

  @Override
  @SuppressWarnings("rawtypes")
  public void transform(
    ITestAnnotation annotation,
    Class testClass,
    Constructor testConstructor,
    Method testMethod
  ) {
    Class currentAnalyzer = annotation.getRetryAnalyzerClass();
    if (
      currentAnalyzer == null || currentAnalyzer == DisabledRetryAnalyzer.class
    ) {
      annotation.setRetryAnalyzer(RetryAnalyzer.class);
    }
  }
}
