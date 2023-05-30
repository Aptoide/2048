package com.appcoins.eskills2048.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BonusUser {
  @SerializedName("rank") @Expose private long rank;
  @SerializedName("bonus_amount") @Expose private float bonusAmount;
  @SerializedName("user_name") @Expose private String userName;
  @SerializedName("score") @Expose private double score;

  public long getRank() {
    return rank;
  }

  public float getBonusAmount() {
    return bonusAmount;
  }

  public double getScore() {
    return score;
  }

  public String getUserName() {
    return userName;
  }
}
