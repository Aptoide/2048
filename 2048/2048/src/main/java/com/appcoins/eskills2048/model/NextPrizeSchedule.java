package com.appcoins.eskills2048.model;

import com.google.gson.annotations.SerializedName;

public class NextPrizeSchedule {
  @SerializedName("next_schedule")
  private long nextSchedule;

  public long getNextSchedule() {
    return nextSchedule;
  }
}
