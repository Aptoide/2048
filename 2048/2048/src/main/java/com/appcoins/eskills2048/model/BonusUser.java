package com.appcoins.eskills2048.model;

import com.appcoins.eskills2048.R;
import com.google.gson.annotations.SerializedName;

public class BonusUser implements RankingsItem {
  private long rank;
  @SerializedName("bonus_amount") private float bonusAmount;
  @SerializedName("user_name") private String userName;
  private double score;


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

  @Override public int getItemType() {
    return R.layout.player_rank_layout;
  }
}
