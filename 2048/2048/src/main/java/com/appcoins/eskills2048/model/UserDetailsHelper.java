package com.appcoins.eskills2048.model;

import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton public class UserDetailsHelper {
  private User currentUser;

  @Inject public UserDetailsHelper() {
  }

  public User getNextOpponent(List<User> opponents) {
    if (opponents.size() == 1 || currentUser == null) {
      currentUser = opponents.get(0);
      return currentUser;
    }

    for (User opponent : opponents) {
      if (!opponent.getWalletAddress()
          .equalsIgnoreCase(currentUser.getWalletAddress())) {
        currentUser = opponent;
        break;
      }
    }
    return currentUser;
  }
}
