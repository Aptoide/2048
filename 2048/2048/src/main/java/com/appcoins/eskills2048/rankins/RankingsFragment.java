package com.appcoins.eskills2048.rankins;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;
import com.appcoins.eskills2048.R;
import com.appcoins.eskills2048.model.MatchDetails;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class RankingsFragment extends Fragment {

  private static final String WALLET_ADDRESS_KEY = "WALLET_ADDRESS_KEY";
  private static final String MATCH_ENVIRONMENT = "MATCH_ENVIRONMENT";

  public static RankingsFragment newInstance(String userWalletAddress,
      MatchDetails.Environment matchEnvironment) {
    Bundle args = new Bundle();
    args.putString(WALLET_ADDRESS_KEY, userWalletAddress);
    args.putSerializable(MATCH_ENVIRONMENT, matchEnvironment);
    RankingsFragment fragment = new RankingsFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Nullable @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_rankings, container, false);
  }

  @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    String walletAddress = null;
    MatchDetails.Environment matchEnvironment = null;
    if (getArguments() != null) {
      walletAddress = getArguments().getString(WALLET_ADDRESS_KEY);
      matchEnvironment =
          (MatchDetails.Environment) getArguments().getSerializable(MATCH_ENVIRONMENT);
    }
    RankingsPagerAdapter rankingsPagerAdapter =
        new RankingsPagerAdapter(this, walletAddress, matchEnvironment);
    ViewPager2 viewPager = view.findViewById(R.id.pager);
    viewPager.setAdapter(rankingsPagerAdapter);
    TabLayout tabLayout = view.findViewById(R.id.tab_layout);
    new TabLayoutMediator(tabLayout, viewPager,
        (tab, position) -> tab.setText(rankingsPagerAdapter.getFragmentTitle(position))).attach();
  }
}
