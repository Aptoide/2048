package com.tpcstld.twozerogame.usecase;

import com.tpcstld.twozerogame.model.RoomResponse;
import com.tpcstld.twozerogame.repository.RoomRepository;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

public class GetRoomUseCase {

  private final RoomRepository roomRepository;

  public GetRoomUseCase(RoomRepository roomRepository) {
    this.roomRepository = roomRepository;
  }

  public Single<RoomResponse> getRoom(String roomId) {
    return roomRepository.getRoom(roomId)
        .subscribeOn(Schedulers.io());
  }
}
