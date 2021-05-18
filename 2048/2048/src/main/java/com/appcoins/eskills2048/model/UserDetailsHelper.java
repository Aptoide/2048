package com.appcoins.eskills2048.model;

import java.util.List;

public class UserDetailsHelper {
    private User currentUser;

    public User getNextOpponent(List<User> opponents) {
        if (opponents.size() == 1 || currentUser == null) {
            currentUser = opponents.get(0);
            return currentUser;
        }

        for (User opponent : opponents) {
            if (!opponent.getWalletAddress().equalsIgnoreCase(
                    currentUser.getWalletAddress())) {
                currentUser = opponent;
                break;
            }
        }
        return currentUser;
    }
}
