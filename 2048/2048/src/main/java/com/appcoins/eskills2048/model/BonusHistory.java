package com.appcoins.eskills2048.model;

import java.util.List;

public class BonusHistory {

  private String date;
  private List<BonusUser> users;


  public String getDate() {
    return date;
  }

  public List<BonusUser> getUsers() {
    return users;
  }
}
