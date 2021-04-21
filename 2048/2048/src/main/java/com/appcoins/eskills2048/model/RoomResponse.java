package com.appcoins.eskills2048.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import lombok.Data;
import lombok.ToString;

@ToString @Data public class RoomResponse {

  @SerializedName("room_id") private String roomId;
  @SerializedName("winner") private Winner winner;
  @SerializedName("current_user") private User currentUser;
  @SerializedName("package_name") private String packageName;
  @SerializedName("status") private RoomStatus status;
  @SerializedName("room_stake") private RoomStake roomStake;
  @SerializedName("users") private List<User> users;
}
