package com.appcoins.eskills2048.model;

import com.google.gson.annotations.SerializedName;

public class RoomResult {
    @SerializedName("winner")
    private User winner;
    @SerializedName("winner_tx")
    private String winnerTx;
    @SerializedName("winner_amount")
    private float winnerAmount;

    public User getWinner() {
        return winner;
    }

    public float getWinnerAmount() {
        return winnerAmount;
    }
}
