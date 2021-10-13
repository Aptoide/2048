package com.appcoins.eskills2048.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GeneralPlayerStatsResponse {

    @SerializedName("current_user")
    @Expose
    private UserRankings player;

    @SerializedName("top_3")
    @Expose
    private UserRankings[] top3;

    @SerializedName("above_user")
    @Expose
    private UserRankings[] aboveUser;

    @SerializedName("same_rank")
    @Expose
    private UserRankings[] sameRank;

    @SerializedName("below_user")
    @Expose
    private UserRankings[] belowUser;

    public UserRankings[] getTop3() {
        return top3;
    }

    public UserRankings[] getAboveUser() {
        return aboveUser;
    }

    public UserRankings[] getSameRank() {
        return sameRank;
    }

    public UserRankings[] getBelowUser() {
        return belowUser;
    }

    public UserRankings getPlayer() {
        return player;
    }

}
