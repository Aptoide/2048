package com.appcoins.eskills2048.model;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class User {

    @SerializedName("wallet_address")
    private String walletAddress;
    @SerializedName("user_id")
    private String userId;
    @SerializedName("user_name")
    private String userName;
    @SerializedName("ticket_id")
    private String ticketId;
    @SerializedName("room_metadata")
    private Map<String, String> roomMetadata;
    @SerializedName("status")
    private UserStatus status;
    @SerializedName("score")
    private int score;
    @SerializedName("rank")
    private int rank;

    public String getWalletAddress() {
        return walletAddress;
    }

    public String getUserName() {
        return userName;
    }

    public UserStatus getStatus() {
        return status;
    }

    public int getScore() {
        return score;
    }

    public int getRank() {
        return rank;
    }
}
