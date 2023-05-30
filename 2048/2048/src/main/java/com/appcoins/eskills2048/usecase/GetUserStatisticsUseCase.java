package com.appcoins.eskills2048.usecase;

import com.appcoins.eskills2048.model.TopNPlayersResponse;
import com.appcoins.eskills2048.repository.StatisticsRepository;
import com.appcoins.eskills2048.repository.StatisticsTimeFrame;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton public class GetUserStatisticsUseCase {
  private final StatisticsRepository statisticsRepository;

  @Inject public GetUserStatisticsUseCase(StatisticsRepository statisticsRepository) {
    this.statisticsRepository = statisticsRepository;
  }

  public Single<TopNPlayersResponse> execute(String applicationId, String userWalletAddress,
      StatisticsTimeFrame timeFrame) {
    return statisticsRepository.getTopNPlayers(applicationId, userWalletAddress, timeFrame)
        .subscribeOn(Schedulers.io());
  }
}
