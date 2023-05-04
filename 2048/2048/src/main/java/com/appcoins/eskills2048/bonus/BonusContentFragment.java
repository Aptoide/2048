package com.appcoins.eskills2048.bonus;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.appcoins.eskills2048.BuildConfig;
import com.appcoins.eskills2048.R;
import com.appcoins.eskills2048.model.BonusHistory;
import com.appcoins.eskills2048.model.UserRankings;
import com.appcoins.eskills2048.model.UserRankingsItem;
import com.appcoins.eskills2048.repository.StatisticsTimeFrame;
import com.appcoins.eskills2048.usecase.GetBonusHistoryUseCase;
import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

@AndroidEntryPoint public class BonusContentFragment extends Fragment {

  private static final String TIME_FRAME_KEY = "TIME_FRAME_KEY";

  private static final String SKU_KEY = "SKU_KEY";
  private StatisticsTimeFrame timeFrame;
  private String sku;
  private BonusAdapter adapter;
  private final CompositeDisposable disposables = new CompositeDisposable();
  private View loadingView;
  private RecyclerView recyclerView;
  private View errorView;

  @Inject GetBonusHistoryUseCase getBonusHistoryUseCase;

  public static BonusContentFragment newInstance(String sku, StatisticsTimeFrame timeFrame) {
    Bundle args = new Bundle();
    args.putSerializable(TIME_FRAME_KEY, timeFrame);
    args.putSerializable(SKU_KEY, sku);
    BonusContentFragment fragment = new BonusContentFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Bundle arguments = getArguments();
    if (arguments != null) {
      timeFrame = (StatisticsTimeFrame) arguments.getSerializable(TIME_FRAME_KEY);
    }
  }

  @Nullable @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_rankings_tab_content, container, false);
  }

  @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    recyclerView = view.findViewById(R.id.rankingsRecyclerView);
    adapter = new BonusAdapter(LayoutInflater.from(getContext()));
    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    recyclerView.setAdapter(adapter);
    loadingView = view.findViewById(R.id.loading);
    errorView = view.findViewById(R.id.error_view);
    showBonus();
    view.findViewById(R.id.retry_button)
        .setOnClickListener(view1 -> showBonus());
  }

  private void showBonus() {
    disposables.add(getBonusHistoryUseCase.execute(BuildConfig.APPLICATION_ID, sku, timeFrame)
        .observeOn(AndroidSchedulers.mainThread())
        .doOnSubscribe(disposable -> showLoadingView())
        .doOnSuccess(disposable -> showRecyclerView())
        .subscribe(this::updateBonusList, throwable -> {
          throwable.printStackTrace();
          showErrorView();
        }));
  }

  private void showErrorView() {
    errorView.setVisibility(View.VISIBLE);
    loadingView.setVisibility(View.GONE);
    recyclerView.setVisibility(View.GONE);
  }

  private void showRecyclerView() {
    loadingView.setVisibility(View.GONE);
    errorView.setVisibility(View.GONE);
    recyclerView.setVisibility(View.VISIBLE);
  }

  private void showLoadingView() {
    loadingView.setVisibility(View.VISIBLE);
    errorView.setVisibility(View.GONE);
    recyclerView.setVisibility(View.GONE);
  }

  private void updateBonusList(BonusHistory bonusHistory) {
    // TODO
  }

  private List<UserRankingsItem> mapPlayers(UserRankings[] players, String rankingWalletAddress) {
    // TODO
    ArrayList<UserRankingsItem> playersList = new ArrayList<>();
    for (UserRankings player : players) {
      playersList.add(new UserRankingsItem(player.getRankingUsername(), player.getRankingScore(),
          player.getRankPosition(),
          rankingWalletAddress.equalsIgnoreCase(player.getRankingWalletAddress())));
    }
    return playersList;
  }

  @Override public void onDestroyView() {
    disposables.clear();
    super.onDestroyView();
  }
}
