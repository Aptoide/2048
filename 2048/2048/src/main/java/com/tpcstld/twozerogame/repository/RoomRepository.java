package com.tpcstld.twozerogame.repository;

import com.tpcstld.twozerogame.api.RoomApi;
import com.tpcstld.twozerogame.model.RoomRequest;
import com.tpcstld.twozerogame.model.RoomResponse;
import io.reactivex.Single;

public class RoomRepository {

  private final RoomApi roomApi;

  public RoomRepository(RoomApi roomApi) {
    this.roomApi = roomApi;
  }

  public Single<RoomResponse> patch(String roomId, String userId, String walletAddress, String jwt,
      int score) {
    RoomRequest roomRequest = new RoomRequest();

    roomRequest.setUserId(userId);
    roomRequest.setWalletAddress(walletAddress);
    roomRequest.setJwt(jwt);
    roomRequest.setScore(score);

    return roomApi.patchRoom(roomId, roomRequest);
  }
}
