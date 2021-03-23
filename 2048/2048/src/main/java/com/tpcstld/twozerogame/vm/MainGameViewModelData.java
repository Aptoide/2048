package com.tpcstld.twozerogame.vm;

import lombok.Data;

@Data
public class MainGameViewModelData {

  private final String roomId;
  private final String userId;
  private final String walletAddress;
  private final String jwt;

  public MainGameViewModelData(String roomId, String userId, String walletAddress, String jwt) {
    this.roomId = roomId;
    this.userId = userId;
    this.walletAddress = walletAddress;
    this.jwt = jwt;
  }
}
