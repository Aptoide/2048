package com.appcoins.eskills2048.model;

import com.appcoins.eskills2048.R;

public class UserRankingsItem implements RankingsItem {
  private final String userName;
  private final double score;
  private final long rank;
  private final boolean isCurrentUser;

  public UserRankingsItem(String userName, double score, long rank, boolean isCurrentUser) {
    this.userName = userName;
    this.score = score;
    this.rank = rank;
    this.isCurrentUser = isCurrentUser;
  }

  @Override public int getItemType() {
    return R.layout.player_rank_layout;
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

  public boolean isCurrentUser() {
    return isCurrentUser;
  }
}
