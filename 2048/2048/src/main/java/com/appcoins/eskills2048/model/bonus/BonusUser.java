package com.appcoins.eskills2048.model.bonus;

import com.google.gson.annotations.SerializedName;

public class BonusUser {
  private int rank;
  @SerializedName("bonus_amount") private int bonusAmount;
  @SerializedName("user_name") private String userName;
  private int score;


  public int getRank() {
    return rank;
  }

  public int getBonusAmount() {
    return bonusAmount;
  }

  public int getScore() {
    return score;
  }

  public String getUserName() {
    return userName;
  }
}
