package com.appcoins.eskills2048.model;

public class MatchDetails {
  private final String product;
  private final float value;
  private final String currency;
  private final int numberOfUsers;
  private final int timeout;

  public MatchDetails(String product, float value, String currency,
      int numberOfUsers, int timeout) {
    this.product = product;
    this.value = value;
    this.currency = currency;
    this.numberOfUsers = numberOfUsers;
    this.timeout = timeout;
  }

  public String getProduct() {
    return product;
  }

  public float getValue() {
    return value;
  }

  public String getCurrency() {
    return currency;
  }

  public int getNumberOfUsers() {
    return numberOfUsers;
  }

  public int getTimeout() {
    return timeout;
  }

  public enum Environment {
    SANDBOX, LIVE
  }
}
