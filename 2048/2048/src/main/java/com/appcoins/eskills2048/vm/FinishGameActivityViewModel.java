package com.appcoins.eskills2048.vm;

import com.appcoins.eskills2048.model.RoomResponse;
import com.appcoins.eskills2048.model.RoomStatus;
import com.appcoins.eskills2048.model.User;
import com.appcoins.eskills2048.model.UserStatus;
import com.appcoins.eskills2048.usecase.GetRoomUseCase;
import io.reactivex.Single;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class FinishGameActivityViewModel {

  private final GetRoomUseCase getRoomUseCase;
  private final String session;

  public FinishGameActivityViewModel(GetRoomUseCase getRoomUseCase, String session) {
    this.getRoomUseCase = getRoomUseCase;
    this.session = session;
  }

  public Single<Boolean> isWinner() {
    return getRoomUseCase.getRoom(session)
        .toObservable()
        .repeatWhen(objectFlowable -> objectFlowable.delay(3, TimeUnit.SECONDS))
        .skipWhile(this::isInProgress)
        .map(this::isWinner)
        .take(1)
        .singleOrError();
  }

  private boolean isWinner(RoomResponse roomResponse) {
    String winnerWalletAddress = roomResponse.getWinner()
        .getWalletAddress();

    String currentUserWalletAddress = roomResponse.getCurrentUser()
        .getWalletAddress();

    return winnerWalletAddress.equalsIgnoreCase(currentUserWalletAddress);
  }

  private boolean isInProgress(RoomResponse roomResponse) {
    boolean completed = roomResponse.getStatus() == RoomStatus.COMPLETED;

    List<User> users = roomResponse.getUsers();
    if (completed && (users.get(0)
        .getStatus() == UserStatus.PLAYING
        || users.get(1)
        .getStatus() == UserStatus.PLAYING)) {
      throw new IllegalStateException("Match Completed but some players are still playing!");
    }

    return !completed;
  }
}
