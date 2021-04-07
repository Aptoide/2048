package com.tpcstld.twozerogame.vm;

import lombok.Data;

@Data
public class MainGameViewModelData {

  private final String roomId;
  private final String userId;
  private final String walletAddress;
  private final String session;

  public MainGameViewModelData(String roomId, String userId, String walletAddress, String session) {
    this.roomId = roomId;
    this.userId = userId;
    this.walletAddress = walletAddress;
    this.session = session;
  }
}
