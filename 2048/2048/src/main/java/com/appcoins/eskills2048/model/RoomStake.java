package com.appcoins.eskills2048.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class RoomStake {

  @SerializedName("appc") private double appc;
  @SerializedName("usd") private int usd;
}
