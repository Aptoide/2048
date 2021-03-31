package com.tpcstld.twozerogame.model;

import com.google.gson.annotations.SerializedName;
import java.util.Map;

public class User {

  @SerializedName("wallet_address") private String walletAddress;
  @SerializedName("user_id") private String userId;
  @SerializedName("ticket_id") private String ticketId;
  @SerializedName("room_metadata") private Map<String, String> roomMetadata;
  @SerializedName("status") private UserStatus status;
  @SerializedName("score") private int score;

  public String getWalletAddress() {
    return walletAddress;
  }

  public void setWalletAddress(String walletAddress) {
    this.walletAddress = walletAddress;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public Map<String, String> getRoomMetadata() {
    return roomMetadata;
  }

  public void setRoomMetadata(Map<String, String> roomMetadata) {
    this.roomMetadata = roomMetadata;
  }

  public int getScore() {
    return score;
  }

  public void setScore(int score) {
    this.score = score;
  }
}
