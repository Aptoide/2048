package com.appcoins.eskills2048.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TopNPlayersResponse {

  @SerializedName("current_user") @Expose private TopRankings currentUser;
  @SerializedName("user_list") @Expose private TopRankings[] userList;

  public TopRankings getCurrentUser() {
    return currentUser;
  }

  public TopRankings[] getUserList() {
    return userList;
  }
}

