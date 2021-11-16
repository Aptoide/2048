package com.appcoins.eskills2048.usecase;

import com.appcoins.eskills2048.model.RoomResponse;
import com.appcoins.eskills2048.model.UserStatus;
import com.appcoins.eskills2048.repository.RoomRepository;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton public class SetScoreUseCase {

  private final RoomRepository roomRepository;

  @Inject public SetScoreUseCase(RoomRepository roomRepository) {
    this.roomRepository = roomRepository;
  }

  public Single<RoomResponse> setScore(String session, long score) {
    return roomRepository.patch(session, score, UserStatus.PLAYING)
        .subscribeOn(Schedulers.io());
  }
}
