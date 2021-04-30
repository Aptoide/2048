package com.appcoins.eskills2048.usecase;

import com.appcoins.eskills2048.model.RoomResponse;
import com.appcoins.eskills2048.repository.RoomRepository;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

public class GetRoomUseCase {

  private final RoomRepository roomRepository;

  public GetRoomUseCase(RoomRepository roomRepository) {
    this.roomRepository = roomRepository;
  }

  public Single<RoomResponse> getRoom(String session) {
    return roomRepository.getRoom(session)
        .subscribeOn(Schedulers.io());
  }
}