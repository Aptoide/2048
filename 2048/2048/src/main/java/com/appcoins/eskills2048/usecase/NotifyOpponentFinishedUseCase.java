package com.appcoins.eskills2048.usecase;

import com.appcoins.eskills2048.OpponentStatusListener;
import com.appcoins.eskills2048.model.User;
import com.appcoins.eskills2048.model.UserStatus;
import java.util.ArrayList;
import java.util.List;

public class NotifyOpponentFinishedUseCase {
  private final List<String> finishedOpponents = new ArrayList<>();
  private final OpponentStatusListener opponentStatusListener;

  public NotifyOpponentFinishedUseCase(OpponentStatusListener opponentStatusListener) {
    this.opponentStatusListener = opponentStatusListener;
  }

  public void notify(User opponent) {
    if (finishedOpponents.contains(opponent.getWalletAddress())) {
      return;
    }

    if (opponent.getStatus() != UserStatus.PLAYING) {
      opponentStatusListener.onOpponentFinished(opponent);
      finishedOpponents.add(opponent.getWalletAddress());
    }
  }
}
