package com.appcoins.eskills2048.usecase;

import com.appcoins.eskills2048.model.GeneralPlayerStatsResponse;
import com.appcoins.eskills2048.model.MatchDetails;
import com.appcoins.eskills2048.model.UserRankings;
import com.appcoins.eskills2048.repository.StatisticsRepository;
import com.appcoins.eskills2048.repository.StatisticsTimeFrame;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import java.util.Arrays;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton public class GetUserStatisticsUseCase {
  private final StatisticsRepository statisticsRepository;

  @Inject public GetUserStatisticsUseCase(StatisticsRepository statisticsRepository) {
    this.statisticsRepository = statisticsRepository;
  }

  public Single<UserRankings[]> execute(String applicationId, String userWalletAddress,
      MatchDetails.Environment matchEnvironment, StatisticsTimeFrame timeFrame) {
    return statisticsRepository.getUserStatistics(applicationId, userWalletAddress,
            matchEnvironment, timeFrame)
        .subscribeOn(Schedulers.io())
        .map(this::mergeRankings);
  }

  private UserRankings[] mergeRankings(GeneralPlayerStatsResponse rankings) {
    UserRankings[] top3 = rankings.getTop3();
    UserRankings[] aboveUser = rankings.getAboveUser();
    UserRankings[] belowUser = rankings.getBelowUser();
    UserRankings player = rankings.getPlayer();

    if (containsUser(top3, player)) {
      return top3;
    }

    UserRankings[] result =
        Arrays.copyOf(top3, top3.length + aboveUser.length + belowUser.length + 1);
    System.arraycopy(aboveUser, 0, result, top3.length, aboveUser.length);
    result[top3.length + aboveUser.length] = player;
    System.arraycopy(belowUser, 0, result, top3.length + aboveUser.length + 1, belowUser.length);

    return result;
  }

  private boolean containsUser(UserRankings[] users, UserRankings user) {
    for (UserRankings u : users) {
      if (u.getRankingWalletAddress()
          .equalsIgnoreCase(user.getRankingWalletAddress())) {
        return true;
      }
    }
    return false;
  }
}
