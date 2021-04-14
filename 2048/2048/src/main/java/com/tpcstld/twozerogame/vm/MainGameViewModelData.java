package com.tpcstld.twozerogame.vm;

import lombok.Data;

@Data
public class MainGameViewModelData {

  private final String userId;
  private final String walletAddress;
  private final String session;

  public MainGameViewModelData(String userId, String walletAddress, String session) {
    this.userId = userId;
    this.walletAddress = walletAddress;
    this.session = session;
  }
}
