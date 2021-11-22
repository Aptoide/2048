package com.appcoins.eskills2048.api;

import com.appcoins.eskills2048.model.GeneralPlayerStatsResponse;
import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface GeneralPlayerStats {

  @Headers("accept: application/json") @GET("room/statistics/general_player_stats")
  Single<GeneralPlayerStatsResponse> getGeneralPlayerStats(
      @Query("package_name") String packageName, @Query("wallet_address") String walletAddress,
      @Query("product") String product, @Query("ranks_above_user") int ranksAbove,
      @Query("ranks_below_user") int ranksBelow, @Query("match_environment") String matchEnvironment,
      @Query("time_frame") String timeframe);
}
