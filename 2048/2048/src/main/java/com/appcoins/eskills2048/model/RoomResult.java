package com.appcoins.eskills2048.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.ToString;

@ToString @Data public class RoomResult {

  @SerializedName("winner") private User winner;
  @SerializedName("winner_tx") private String winnerTx;
  @SerializedName("winner_amount") private float winnerAmount;
}
