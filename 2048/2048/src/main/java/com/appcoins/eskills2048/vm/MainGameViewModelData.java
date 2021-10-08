package com.appcoins.eskills2048.vm;

public class MainGameViewModelData {

  private final String userId;
  private final String walletAddress;
  private final String session;

  public MainGameViewModelData(String userId, String walletAddress, String session) {
    this.userId = userId;
    this.walletAddress = walletAddress;
    this.session = session;
  }

  public String getWalletAddress() {
    return walletAddress;
  }

  public String getSession() {
    return session;
  }
}
