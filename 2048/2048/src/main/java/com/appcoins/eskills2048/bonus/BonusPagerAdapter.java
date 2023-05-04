package com.appcoins.eskills2048.bonus;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.appcoins.eskills2048.R;
import com.appcoins.eskills2048.repository.StatisticsTimeFrame;
import java.security.InvalidParameterException;

class BonusPagerAdapter extends FragmentStateAdapter {

  private final String sku;


  public BonusPagerAdapter(@NonNull Fragment fragment, String sku) {
    super(fragment);
    this.sku = sku;
  }

  @NonNull @Override public Fragment createFragment(int position) {
    return BonusContentFragment.newInstance(sku,
        getTimeFrame(position));
  }

  private StatisticsTimeFrame getTimeFrame(int position) {
    switch (position) {
      case 0:
        return StatisticsTimeFrame.TODAY;
      case 1:
        return StatisticsTimeFrame.WEEK;
      default:
        throw new InvalidParameterException("Invalid position " + position);
    }
  }

  @Override public int getItemCount() {
    return 2;
  }

  public int getFragmentTitle(int position) {
    switch (getTimeFrame(position)) {
      case TODAY:
        return R.string.rankings_today;
      case WEEK:
        return R.string.rankings_week;
      default:
        throw new InvalidParameterException("Invalid position " + position);
    }
  }
}
