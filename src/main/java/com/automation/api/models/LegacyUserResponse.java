package com.automation.api.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LegacyUserResponse {

  private LegacyUser data;

  public LegacyUser getData() {
    return data;
  }

  public void setData(LegacyUser data) {
    this.data = data;
  }
}
