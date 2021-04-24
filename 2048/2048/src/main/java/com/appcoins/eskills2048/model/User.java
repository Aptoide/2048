package com.appcoins.eskills2048.model;

import com.google.gson.annotations.SerializedName;
import java.util.Map;

public class User {

  @SerializedName("wallet_address") private String walletAddress;
  @SerializedName("user_id") private String userId;
  @SerializedName("user_name") private String userName;
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

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getTicketId() {
    return ticketId;
  }

  public void setTicketId(String ticketId) {
    this.ticketId = ticketId;
  }

  public Map<String, String> getRoomMetadata() {
    return roomMetadata;
  }

  public void setRoomMetadata(Map<String, String> roomMetadata) {
    this.roomMetadata = roomMetadata;
  }

  public UserStatus getStatus() {
    return status;
  }

  public void setStatus(UserStatus status) {
    this.status = status;
  }

  public int getScore() {
    return score;
  }

  public void setScore(int score) {
    this.score = score;
  }
}
