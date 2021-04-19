package com.appcoins.eskills2048.usecase;

import com.appcoins.eskills2048.model.RoomResponse;
import com.appcoins.eskills2048.model.UserStatus;
import com.appcoins.eskills2048.repository.RoomRepository;
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
