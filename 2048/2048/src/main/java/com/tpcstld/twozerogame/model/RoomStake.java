package com.tpcstld.twozerogame.model;

import com.google.gson.annotations.SerializedName;

public class RoomStake {

  @SerializedName("appc") private double appc;
  @SerializedName("usd") private int usd;

  public double getAppc() {
    return appc;
  }

  public void setAppc(double appc) {
    this.appc = appc;
  }

  public int getUsd() {
    return usd;
  }

  public void setUsd(int usd) {
    this.usd = usd;
  }
}
