package com.appcoins.eskills2048.usecase;

import com.appcoins.eskills2048.model.BonusHistory;
import com.appcoins.eskills2048.repository.BonusRepository;
import com.appcoins.eskills2048.repository.StatisticsTimeFrame;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton public class GetBonusHistoryUseCase {
  private final BonusRepository bonusRepository;

  @Inject public GetBonusHistoryUseCase(BonusRepository bonusRepository) {
    this.bonusRepository = bonusRepository;
  }

  public Single<List<BonusHistory>> execute(String packageName, String sku, StatisticsTimeFrame timeFrame) {
    return bonusRepository.getBonusHistoryList(packageName, sku, timeFrame)
        .subscribeOn(Schedulers.io());
  }
}
