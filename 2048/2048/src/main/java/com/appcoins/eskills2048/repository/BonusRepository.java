package com.appcoins.eskills2048.repository;

import com.appcoins.eskills2048.api.BonusPrizeApi;
import com.appcoins.eskills2048.model.bonus.BonusHistory;
import com.appcoins.eskills2048.model.bonus.NextPrizeSchedule;
import io.reactivex.Single;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton public class BonusRepository {
  private final BonusPrizeApi bonusPrizeApi;


  @Inject public BonusRepository(BonusPrizeApi bonusPrizeApi) {
    this.bonusPrizeApi = bonusPrizeApi;
  }

  public Single<BonusHistory> getBonusHistoryList(String packageName, String sku, StatisticsTimeFrame timeFrame){
    return bonusPrizeApi.getBonusHistoryList(packageName, sku, timeFrame);
  }
  public Single<NextPrizeSchedule> getNextBonusSchedule(StatisticsTimeFrame timeFrame) {
    return bonusPrizeApi.getTimeUntilNextBonus(timeFrame);
  }
}
