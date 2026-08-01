package com.automation.api.fixtures;

import java.util.concurrent.atomic.AtomicInteger;

public class UserFixtures {

  private static final AtomicInteger sequence = new AtomicInteger(1000);

  private UserFixtures() {}

  public static int freshMutableUserId() {
    return sequence.incrementAndGet();
  }
}
