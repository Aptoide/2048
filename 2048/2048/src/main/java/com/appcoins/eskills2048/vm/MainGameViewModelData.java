package com.appcoins.eskills2048.vm;

import com.appcoins.eskills2048.model.LocalGameStatus;

public class MainGameViewModelData {

  private final String userId;
  private final String walletAddress;
  private final String session;
  private final LocalGameStatus localGameStatus;

  public MainGameViewModelData(String userId, String walletAddress, String session,
      LocalGameStatus localGameStatus) {
    this.userId = userId;
    this.walletAddress = walletAddress;
    this.session = session;
    this.localGameStatus = localGameStatus;
  }

  public String getWalletAddress() {
    return walletAddress;
  }

  public String getSession() {
    return session;
  }

  public LocalGameStatus getLocalGameStatus() {
    return localGameStatus;
  }
}
