package com.appcoins.eskills2048.repository;

import com.appcoins.eskills2048.api.GeneralPlayerStats;
import com.appcoins.eskills2048.model.GeneralPlayerStatsResponse;
import com.appcoins.eskills2048.model.MatchDetails;

import io.reactivex.Single;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton public class StatisticsRepository {
  private final GeneralPlayerStats api;

  @Inject public StatisticsRepository(GeneralPlayerStats api) {
    this.api = api;
  }

  public Single<GeneralPlayerStatsResponse> getUserStatistics(String applicationId,
      String userWalletAddress, MatchDetails.Environment matchEnvironment,
      StatisticsTimeFrame timeFrame) {
    return api.getGeneralPlayerStats(applicationId, userWalletAddress, null, 3, 2,
        matchEnvironment.toString(), timeFrame.name());
  }
}
