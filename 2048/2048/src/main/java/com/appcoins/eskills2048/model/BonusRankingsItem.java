package com.appcoins.eskills2048.model;

import com.appcoins.eskills2048.R;

public class BonusRankingsItem {
  private final String userName;
  private final double score;
  private final long rank;

  private final float bonusAmount;
  private final boolean isCurrentUser;

  public BonusRankingsItem(String userName, double score, long rank, float bonusAmount, boolean isCurrentUser) {
    this.userName = userName;
    this.score = score;
    this.rank = rank;
    this.bonusAmount = bonusAmount;
    this.isCurrentUser = isCurrentUser;
  }

  public int getItemType() {
    return R.layout.player_bonus_layout;
  }

  public String getUserName() {
    return userName;
  }

  public double getScore() {
    return score;
  }

  public long getRank() {
    return rank;
  }

  public float getBonusAmount() {
    return bonusAmount;
  }

  public boolean isCurrentUser() {
    return isCurrentUser;
  }
}
