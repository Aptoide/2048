package com.appcoins.eskills2048.repository;

import com.appcoins.eskills2048.api.GeneralPlayerStats;
import com.appcoins.eskills2048.model.TopNPlayersResponse;
import io.reactivex.Single;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton public class StatisticsRepository {
  private static final int RANKINGS_LIMIT = 10;
  private final GeneralPlayerStats api;

  @Inject public StatisticsRepository(GeneralPlayerStats api) {
    this.api = api;
  }

  public Single<TopNPlayersResponse> getTopNPlayers(String applicationId, String walletAddress,
      StatisticsTimeFrame timeFrame) {
    return api.getTopNPlayers(applicationId, walletAddress, timeFrame.name(), RANKINGS_LIMIT);
  }
}
