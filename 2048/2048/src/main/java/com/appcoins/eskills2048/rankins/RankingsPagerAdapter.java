package com.appcoins.eskills2048.rankins;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.appcoins.eskills2048.R;
import com.appcoins.eskills2048.model.MatchDetails;
import com.appcoins.eskills2048.repository.StatisticsTimeFrame;
import java.security.InvalidParameterException;

class RankingsPagerAdapter extends FragmentStateAdapter {

  private final String walletAddress;
  private final MatchDetails.Environment matchEnvironment;

  public RankingsPagerAdapter(@NonNull Fragment fragment, String walletAddress,
      MatchDetails.Environment matchEnvironment) {
    super(fragment);
    this.walletAddress = walletAddress;
    this.matchEnvironment = matchEnvironment;
  }

  @NonNull @Override public Fragment createFragment(int position) {
    return RankingsContentFragment.newInstance(walletAddress, matchEnvironment,
        getTimeFrame(position));
  }

  private StatisticsTimeFrame getTimeFrame(int position) {
    switch (position) {
      case 0:
        return StatisticsTimeFrame.TODAY;
      case 1:
        return StatisticsTimeFrame.WEEK;
      case 2:
        return StatisticsTimeFrame.ALL_TIME;
      default:
        throw new InvalidParameterException("Invalid position " + position);
    }
  }

  @Override public int getItemCount() {
    return 3;
  }

  public int getFragmentTitle(int position) {
    switch (getTimeFrame(position)) {
      case TODAY:
        return R.string.rankings_today;
      case WEEK:
        return R.string.rankings_week;
      case ALL_TIME:
        return R.string.rankings_all_time;
      default:
        throw new InvalidParameterException("Invalid position " + position);
    }
  }
}
