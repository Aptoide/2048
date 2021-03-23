package com.tpcstld.twozerogame.model;

import com.google.gson.annotations.SerializedName;

public class RoomRequest {

  @SerializedName("user_id") private String userId;
  @SerializedName("wallet_address") private String walletAddress;
  @SerializedName("jwt") private String jwt;
  @SerializedName("score") private int score;

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String getWalletAddress() {
    return walletAddress;
  }

  public void setWalletAddress(String walletAddress) {
    this.walletAddress = walletAddress;
  }

  public String getJwt() {
    return jwt;
  }

  public void setJwt(String jwt) {
    this.jwt = jwt;
  }

  public int getScore() {
    return score;
  }

  public void setScore(int score) {
    this.score = score;
  }
}