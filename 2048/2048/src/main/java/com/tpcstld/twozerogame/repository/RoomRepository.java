package com.tpcstld.twozerogame.repository;

import com.tpcstld.twozerogame.api.RoomApi;
import com.tpcstld.twozerogame.model.PatchRoomRequest;
import com.tpcstld.twozerogame.model.RoomResponse;
import com.tpcstld.twozerogame.model.RoomStatus;
import io.reactivex.Single;

public class RoomRepository {

  private final RoomApi roomApi;

  public RoomRepository(RoomApi roomApi) {
    this.roomApi = roomApi;
  }

  public Single<RoomResponse> patch(String roomId, String walletAddress, long score,
      RoomStatus status) {
    PatchRoomRequest patchRoomRequest = new PatchRoomRequest();

    patchRoomRequest.setScore(score);
    patchRoomRequest.setStatus(status);

    return roomApi.patchRoom(roomId, walletAddress, patchRoomRequest);
  }
}
