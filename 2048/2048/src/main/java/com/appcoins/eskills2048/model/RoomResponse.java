package com.appcoins.eskills2048.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.Data;
import lombok.ToString;

@ToString @Data public class RoomResponse {

  @SerializedName("room_id") private String roomId;
  @SerializedName("room_result") private RoomResult roomResult;
  @SerializedName("current_user") private User currentUser;
  @SerializedName("package_name") private String packageName;
  @SerializedName("status") private RoomStatus status;
  @SerializedName("usd_stake") private int stake;
  @SerializedName("users") private List<User> users;

  public List<User> getOpponents(String walletAddress) {
    List<User> sortedUsers = sortUsers(users);
    List<User> opponents = new ArrayList<>();
    for (User user: setRank(sortedUsers)) {
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

  private List<User> setRank(List<User> users){
    for (int i = 0; i < users.size(); i++) {
      User user = users.get(i);
      user.setRank(i + 1);
    }
    return users;
  }
}