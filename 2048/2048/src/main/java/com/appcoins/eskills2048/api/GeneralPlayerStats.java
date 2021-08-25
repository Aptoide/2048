package com.appcoins.eskills2048.api;

import com.appcoins.eskills2048.model.GeneralPlayerStatsResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface GeneralPlayerStats {

    @Headers("accept: application/json")
    @GET("general_player_stats")
    Call<GeneralPlayerStatsResponse> getGeneralPlayerStats(
            @Query("package_name") String packageName,
            @Query("wallet_address") String walletAddress
    );
}
