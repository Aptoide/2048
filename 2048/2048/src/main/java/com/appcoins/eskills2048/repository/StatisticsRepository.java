package com.appcoins.eskills2048.repository;

import com.appcoins.eskills2048.api.GeneralPlayerStats;
import com.appcoins.eskills2048.model.GeneralPlayerStatsResponse;
import io.reactivex.Single;

public class StatisticsRepository {
  private final GeneralPlayerStats api;

  public StatisticsRepository(GeneralPlayerStats api) {
    this.api = api;
  }

  public Single<GeneralPlayerStatsResponse> getUserStatistics(String applicationId,
      String userWalletAddress, String sku, String matchEnvironment, StatisticsTimeFrame timeFrame) {
    return api.getGeneralPlayerStats(applicationId, userWalletAddress, sku, 3, 2, matchEnvironment, timeFrame.name());
  }
}
