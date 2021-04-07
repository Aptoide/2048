package com.tpcstld.twozerogame.repository;

import com.tpcstld.twozerogame.api.RoomApi;
import com.tpcstld.twozerogame.model.PatchRoomRequest;
import com.tpcstld.twozerogame.model.RoomResponse;
import com.tpcstld.twozerogame.model.UserStatus;
import io.reactivex.Single;

public class RoomRepository {

  public static final String BEARER_ = "Bearer ";

  private final RoomApi roomApi;

  public RoomRepository(RoomApi roomApi) {
    this.roomApi = roomApi;
  }

  public Single<RoomResponse> patch(String session, long score, UserStatus status) {
    PatchRoomRequest patchRoomRequest = new PatchRoomRequest();

    patchRoomRequest.setScore(score);
    patchRoomRequest.setStatus(status);

    return roomApi.patchRoom(BEARER_ + session, patchRoomRequest);
  }

  public Single<RoomResponse> getRoom(String roomId) {
    return roomApi.getRoom(roomId);
  }
}
