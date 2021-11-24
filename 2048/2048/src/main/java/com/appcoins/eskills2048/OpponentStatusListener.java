package com.appcoins.eskills2048;

import com.appcoins.eskills2048.model.User;

public interface OpponentStatusListener {
  void onOpponentFinished(User opponent);
}
