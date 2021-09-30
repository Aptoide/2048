package com.appcoins.eskills2048.usecase;

import com.appcoins.eskills2048.model.GeneralPlayerStatsResponse;
import com.appcoins.eskills2048.repository.StatisticsRepository;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

public class GetUserStatisticsUseCase {
  private final StatisticsRepository statisticsRepository;

  public GetUserStatisticsUseCase(StatisticsRepository statisticsRepository) {
    this.statisticsRepository = statisticsRepository;
  }

  public Single<GeneralPlayerStatsResponse> execute(String applicationId,
      String userWalletAddress) {
    return statisticsRepository.getUserStatistics(applicationId, userWalletAddress)
        .subscribeOn(Schedulers.io());
  }
}
