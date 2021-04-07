package com.tpcstld.twozerogame.usecase;

import com.tpcstld.twozerogame.model.RoomResponse;
import com.tpcstld.twozerogame.model.UserStatus;
import com.tpcstld.twozerogame.repository.RoomRepository;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

public class SetFinalScoreUseCase {

  private final RoomRepository roomRepository;

  public SetFinalScoreUseCase(RoomRepository roomRepository) {
    this.roomRepository = roomRepository;
  }

  public Single<RoomResponse> setFinalScore(String session, long score) {
    return roomRepository.patch(session, score, UserStatus.COMPLETED)
        .subscribeOn(Schedulers.io());
  }
}
