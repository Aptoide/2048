package com.appcoins.eskills2048.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class BonusHistory {

  @SerializedName("date") @Expose private String date;
  @SerializedName("users") @Expose private List<BonusUser> users;

  public String getDate() {
    return date;
  }

  public List<BonusUser> getUsers() {
    return users;
  }
}
