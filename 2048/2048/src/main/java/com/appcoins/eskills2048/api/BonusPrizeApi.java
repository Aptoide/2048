package com.appcoins.eskills2048.api;

import com.appcoins.eskills2048.model.BonusHistory;
import com.appcoins.eskills2048.model.NextPrizeSchedule;
import com.appcoins.eskills2048.repository.StatisticsTimeFrame;
import io.reactivex.Single;
import java.util.List;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface BonusPrizeApi {
  @GET("room/bonus/bonus_history/list") Single<List<BonusHistory>> getBonusHistoryList(
      @Query("package_name") String package_name, @Query("sku") String sku,
      @Query("time_frame") StatisticsTimeFrame time_frame);

  @GET("room/bonus/next_schedule") Single<NextPrizeSchedule> getTimeUntilNextBonus(
      @Query("time_frame") StatisticsTimeFrame time_frame);
}
