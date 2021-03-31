package com.tpcstld.twozerogame.model;

import com.google.gson.annotations.SerializedName;

public class PatchRoomRequest {

  @SerializedName("score") private long score;
  @SerializedName("status") private UserStatus status;

  public long getScore() {
    return score;
  }

  public void setScore(long score) {
    this.score = score;
  }

  public UserStatus getStatus() {
    return status;
  }

  public void setStatus(UserStatus status) {
    this.status = status;
  }
}