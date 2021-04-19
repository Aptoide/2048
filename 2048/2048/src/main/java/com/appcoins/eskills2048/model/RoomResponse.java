package com.appcoins.eskills2048.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import lombok.ToString;

@ToString
public class RoomResponse {

  @SerializedName("room_id") private String roomId;
  @SerializedName("package_name") private String packageName;
  @SerializedName("status") private RoomStatus status;
  @SerializedName("users") private List<User> users;
  @SerializedName("room_stake") private RoomStake roomStake;

  public String getRoomId() {
    return roomId;
  }

  public void setRoomId(String roomId) {
    this.roomId = roomId;
  }

  public String getPackageName() {
    return packageName;
  }

  public void setPackageName(String packageName) {
    this.packageName = packageName;
  }

  public List<User> getUsers() {
    return users;
  }

  public void setUsers(List<User> users) {
    this.users = users;
  }

  public RoomStake getRoomStake() {
    return roomStake;
  }

  public void setRoomStake(RoomStake roomStake) {
    this.roomStake = roomStake;
  }
}
