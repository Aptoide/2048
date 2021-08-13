package com.appcoins.eskills2048.vm;

import com.appcoins.eskills2048.model.RoomResponse;
import com.appcoins.eskills2048.model.RoomResult;
import com.appcoins.eskills2048.model.RoomStatus;
import com.appcoins.eskills2048.model.User;
import com.appcoins.eskills2048.model.UserStatus;
import com.appcoins.eskills2048.usecase.GetRoomUseCase;

import java.util.concurrent.TimeUnit;

import io.reactivex.Single;

public class FinishGameActivityViewModel {

  private final GetRoomUseCase getRoomUseCase;
  private final String session;
  private final String walletAddress;

  public FinishGameActivityViewModel(GetRoomUseCase getRoomUseCase, String session, String walletAddress) {
    this.getRoomUseCase = getRoomUseCase;
    this.session = session;
    this.walletAddress = walletAddress;
  }

  public Single<RoomResult> getRoomResult() {
    return getRoomUseCase.getRoom(session)
        .toObservable()
        .repeatWhen(objectFlowable -> objectFlowable.delay(3, TimeUnit.SECONDS))
        .skipWhile(this::isInProgress)
        .map(RoomResponse::getRoomResult)
        .take(1)
        .singleOrError();
  }

  public boolean isWinner(RoomResult roomResult) {
    return roomResult.getWinner().getWalletAddress()
        .equalsIgnoreCase(walletAddress);
  }


  private boolean isInProgress(RoomResponse roomResponse) {
    boolean completed = roomResponse.getStatus() == RoomStatus.COMPLETED;
    for (User user: roomResponse.getUsers()) {
      if (user.getStatus() == UserStatus.PLAYING && completed) {
        throw new IllegalStateException("Match Completed but some players are still playing!");
      }
    }
    return !completed;
  }
}
