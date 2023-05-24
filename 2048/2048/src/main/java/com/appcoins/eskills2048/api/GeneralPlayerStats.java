package com.appcoins.eskills2048.api;

import com.appcoins.eskills2048.model.MatchDetails;
import com.appcoins.eskills2048.model.TopNPlayersResponse;
import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface GeneralPlayerStats {
  @Headers("accept: application/json") @GET("room/statistics/top_n_players")
  Single<TopNPlayersResponse> getTopNPlayers(@Query("package_name") String packageName,
      @Query("product") String sku,
      @Query("match_environment") MatchDetails.Environment matchEnvironment,
      @Query("time_frame") String timeframe, @Query("limit") int limit);
}
