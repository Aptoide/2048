package com.appcoins.eskills2048.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TopRankings {
  @SerializedName("username") @Expose private String username;

  @SerializedName("rank_position") @Expose private int rankPosition;

  @SerializedName("wallet_address") @Expose private String walletAddress;

  @SerializedName("score") @Expose private int score;

  public String getUsername() {
    return username;
  }

  public int getRankPosition() {
    return rankPosition;
  }

  public String getWalletAddress() {
    return walletAddress;
  }

  public int getScore() {
    return score;
  }
}
