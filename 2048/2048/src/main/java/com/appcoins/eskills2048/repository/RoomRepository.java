package com.appcoins.eskills2048.repository;

import com.appcoins.eskills2048.api.RoomApi;
import com.appcoins.eskills2048.model.PatchRoomRequest;
import com.appcoins.eskills2048.model.RoomApiMapper;
import com.appcoins.eskills2048.model.RoomResponse;
import com.appcoins.eskills2048.model.UserStatus;
import io.reactivex.Single;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton public class RoomRepository {

  public static final String BEARER_ = "Bearer ";

  private final RoomApi roomApi;
  private final RoomApiMapper roomApiMapper;

  @Inject public RoomRepository(RoomApi roomApi, RoomApiMapper roomApiMapper) {
    this.roomApiMapper = roomApiMapper;
    this.roomApi = roomApi;
  }

  public Single<RoomResponse> patch(String session, long score, UserStatus status) {
    PatchRoomRequest patchRoomRequest = new PatchRoomRequest();
    patchRoomRequest.setScore(score);
    patchRoomRequest.setStatus(status);
    return roomApiMapper.map(roomApi.patchRoom(BEARER_ + session, patchRoomRequest));
  }

  public Single<RoomResponse> getRoom(String session) {
    return roomApi.getRoom(BEARER_ + session);
  }
}
