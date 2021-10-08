package com.appcoins.eskills2048.model;

import com.google.gson.annotations.SerializedName;

public class UserRankings {

    @SerializedName("username")
    private String rankingUsername;

    @SerializedName("rank_position")
    private int rankPosition;

    @SerializedName("wallet_address")
    private String rankingWalletAddress;

    @SerializedName("score")
    private int rankingScore;

    public String getRankingUsername() {
        if(rankingUsername == null)
            return  rankingWalletAddress;
        return rankingUsername;
    }

    public int getRankPosition() {
        return rankPosition;
    }

    public String getRankingWalletAddress() {
        return rankingWalletAddress;
    }

    public int getRankingScore() {
        return rankingScore;
    }

}
