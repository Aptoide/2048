package com.appcoins.eskills2048.bonus;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;
import com.appcoins.eskills2048.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class BonusFragment extends Fragment {

  private static final String SKU_KEY = "SKU_KEY";

  public static BonusFragment newInstance(String sku) {
    Bundle args = new Bundle();
    args.putString(SKU_KEY, sku);
    BonusFragment fragment = new BonusFragment();
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
    String sku = null;
    if (getArguments() != null) {
      sku = getArguments().getString(SKU_KEY);

    }
    BonusPagerAdapter rankingsPagerAdapter =
        new BonusPagerAdapter(this, sku);
    ViewPager2 viewPager = view.findViewById(R.id.pager);
    viewPager.setAdapter(rankingsPagerAdapter);
    TabLayout tabLayout = view.findViewById(R.id.tab_layout);
    new TabLayoutMediator(tabLayout, viewPager,
        (tab, position) -> tab.setText(rankingsPagerAdapter.getFragmentTitle(position))).attach();
  }
}
