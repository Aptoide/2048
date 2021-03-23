package com.tpcstld.twozerogame.usecase;

import com.tpcstld.twozerogame.model.RoomResponse;
import com.tpcstld.twozerogame.repository.RoomRepository;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

public class SetScoreUseCase {

  private final RoomRepository roomRepository;

  public SetScoreUseCase(RoomRepository roomRepository) {
    this.roomRepository = roomRepository;
  }

  public Single<RoomResponse> setScore(String roomId, String userId, String walletAddress, String jwt, int score) {
    return roomRepository.patch(roomId, userId, walletAddress, jwt, score)
        .subscribeOn(Schedulers.io());
  }
}
