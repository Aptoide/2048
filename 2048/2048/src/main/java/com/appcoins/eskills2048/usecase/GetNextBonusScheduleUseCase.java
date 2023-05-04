package com.appcoins.eskills2048.usecase;

import com.appcoins.eskills2048.model.bonus.NextPrizeSchedule;
import com.appcoins.eskills2048.repository.BonusRepository;
import com.appcoins.eskills2048.repository.StatisticsTimeFrame;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton public class GetNextBonusScheduleUseCase {
  private final BonusRepository bonusRepository;

  @Inject public GetNextBonusScheduleUseCase(BonusRepository bonusRepository) {
    this.bonusRepository = bonusRepository;
  }

  public Single<NextPrizeSchedule> execute(StatisticsTimeFrame timeFrame) {
    return bonusRepository.getNextBonusSchedule(timeFrame)
        .subscribeOn(Schedulers.io());
  }
}
