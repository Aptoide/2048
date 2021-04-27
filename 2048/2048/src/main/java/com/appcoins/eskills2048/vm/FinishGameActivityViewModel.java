package com.appcoins.eskills2048.vm;

import com.appcoins.eskills2048.model.RoomResponse;
import com.appcoins.eskills2048.model.User;
import com.appcoins.eskills2048.model.UserStatus;
import com.appcoins.eskills2048.usecase.GetRoomUseCase;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class FinishGameActivityViewModel {

  private final GetRoomUseCase getRoomUseCase;
  private final String session;
  private final String walletAddress;

  public FinishGameActivityViewModel(GetRoomUseCase getRoomUseCase, String session,
      String walletAddress) {
    this.getRoomUseCase = getRoomUseCase;
    this.session = session;
    this.walletAddress = walletAddress;
  }

  public Single<User> getOpponent() {
    return getRoomUseCase.getRoom(session)
        .map(roomResponse -> {
            List<User> roomUsers = roomResponse.getOpponents(walletAddress);
            return roomUsers.get(0);
        })
        .subscribeOn(Schedulers.io());
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
    List<User> users = roomResponse.getUsers();
    User winner;
    if (users.get(0)
        .getScore() > users.get(1)
        .getScore()) {
      winner = users.get(0);
    } else {
      winner = users.get(1);
    }

    return winner.getWalletAddress()
        .equalsIgnoreCase(walletAddress);
  }

  private boolean isInProgress(RoomResponse roomResponse) {
    List<User> users = roomResponse.getUsers();
    return users.get(0)
        .getStatus() == UserStatus.PLAYING
        || users.get(1)
        .getStatus() == UserStatus.PLAYING;
  }
}
