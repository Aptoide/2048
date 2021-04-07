package com.tpcstld.twozerogame.usecase;

import com.tpcstld.twozerogame.model.RoomResponse;
import com.tpcstld.twozerogame.model.UserStatus;
import com.tpcstld.twozerogame.repository.RoomRepository;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

public class SetScoreUseCase {

  private final RoomRepository roomRepository;

  public SetScoreUseCase(RoomRepository roomRepository) {
    this.roomRepository = roomRepository;
  }

  public Single<RoomResponse> setScore(String session, long score) {
    return roomRepository.patch(session, score, UserStatus.PLAYING)
        .subscribeOn(Schedulers.io());
  }
}
