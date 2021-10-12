package com.appcoins.eskills2048.model;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RoomResponse {

  @SerializedName("room_id") private String roomId;
  @SerializedName("room_result") private RoomResult roomResult;
  @SerializedName("current_user") private User currentUser;
  @SerializedName("package_name") private String packageName;
  @SerializedName("status") private RoomStatus status;
  @SerializedName("usd_stake") private int stake;
  @SerializedName("users") private List<User> users;

  public RoomResult getRoomResult() {
    return roomResult;
  }

  public User getCurrentUser() {
    return currentUser;
  }

  public RoomStatus getStatus() {
    return status;
  }

  public List<User> getUsers() {
    return users;
  }

  public List<User> getOpponents(String walletAddress) {
    List<User> sortedUsers = sortUsers(users);
    List<User> opponents = new ArrayList<>();
    for (User user: sortedUsers) {
      if (!user.getWalletAddress().equalsIgnoreCase(walletAddress)) {
        opponents.add(user);
      }
    }
    return sortUsers(opponents);
  }

  private List<User> sortUsers(List<User> users) {
    Collections.sort(users, (user1, user2) ->
        Integer.valueOf(user2.getScore())
            .compareTo(Integer.valueOf(user1.getScore())));
    return users;
  }
}
