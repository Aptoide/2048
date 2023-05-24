package com.appcoins.eskills2048.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TopNPlayersResponse {
  @SerializedName("user_scores") @Expose private TopRankings[] userScores;

  public TopRankings[] getUserScores() {
    return userScores;
  }
}

