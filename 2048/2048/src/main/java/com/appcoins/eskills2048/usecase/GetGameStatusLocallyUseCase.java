package com.appcoins.eskills2048.usecase;

import com.appcoins.eskills2048.model.LocalGameStatus;
import com.appcoins.eskills2048.model.RoomResponse;
import com.appcoins.eskills2048.model.UserStatus;
import com.appcoins.eskills2048.repository.LocalGameStatusRepository;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton public class GetGameStatusLocallyUseCase {
  private final LocalGameStatusRepository localGameStatusRepository;
  private final GetRoomUseCase getRoomUseCase;

  @Inject public GetGameStatusLocallyUseCase(LocalGameStatusRepository localGameStatusRepository,
      GetRoomUseCase getRoomUseCase) {
    this.localGameStatusRepository = localGameStatusRepository;
    this.getRoomUseCase = getRoomUseCase;
  }

  public LocalGameStatus getGameStatus() {
    LocalGameStatus localGameStatus = localGameStatusRepository.getGameStatus();
    if (localGameStatus == null) {
      return null;
    }

    try {
      RoomResponse roomResponse = getRoomUseCase.getRoom(localGameStatus.getSession())
          .blockingGet();
      if (roomResponse.getCurrentUser()
          .getStatus() == UserStatus.PLAYING) {
        return localGameStatus;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }
}
