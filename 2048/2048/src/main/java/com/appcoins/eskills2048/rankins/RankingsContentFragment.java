package com.appcoins.eskills2048.rankins;

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
import com.appcoins.eskills2048.model.GeneralPlayerStatsResponse;
import com.appcoins.eskills2048.model.MatchDetails;
import com.appcoins.eskills2048.model.RankingsItem;
import com.appcoins.eskills2048.model.RankingsTitle;
import com.appcoins.eskills2048.model.UserRankings;
import com.appcoins.eskills2048.model.UserRankingsItem;
import com.appcoins.eskills2048.repository.StatisticsTimeFrame;
import com.appcoins.eskills2048.usecase.GetUserStatisticsUseCase;
import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

@AndroidEntryPoint public class RankingsContentFragment extends Fragment {
  private static final String WALLET_ADDRESS_KEY = "WALLET_ADDRESS_KEY";
  private static final String TIME_FRAME_KEY = "TIME_FRAME_KEY";
  private static final String MATCH_ENVIRONMENT = "MATCH_ENVIRONMENT";
  private StatisticsTimeFrame timeFrame;
  private MatchDetails.Environment matchEnvironment;
  private String walletAddress;
  private RankingsAdapter adapter;
  private final CompositeDisposable disposables = new CompositeDisposable();
  private View loadingView;
  private RecyclerView recyclerView;
  private View errorView;

  @Inject GetUserStatisticsUseCase getUserStatisticsUseCase;

  public static RankingsContentFragment newInstance(String walletAddress,
                                                    MatchDetails.Environment matchEnvironment, StatisticsTimeFrame timeFrame) {
    Bundle args = new Bundle();
    args.putString(WALLET_ADDRESS_KEY, walletAddress);
    args.putSerializable(MATCH_ENVIRONMENT, matchEnvironment);
    args.putSerializable(TIME_FRAME_KEY, timeFrame);
    RankingsContentFragment fragment = new RankingsContentFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Bundle arguments = getArguments();
    if (arguments != null) {
      timeFrame = (StatisticsTimeFrame) arguments.getSerializable(TIME_FRAME_KEY);
      walletAddress = arguments.getString(WALLET_ADDRESS_KEY);
      matchEnvironment = (MatchDetails.Environment) arguments.getSerializable(MATCH_ENVIRONMENT);
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
    adapter = new RankingsAdapter(LayoutInflater.from(getContext()));
    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    recyclerView.setAdapter(adapter);
    loadingView = view.findViewById(R.id.loading);
    errorView = view.findViewById(R.id.error_view);
    showRankings();
    view.findViewById(R.id.retry_button)
        .setOnClickListener(view1 -> showRankings());
  }

  private void showRankings() {
    disposables.add(
        getUserStatisticsUseCase.execute(BuildConfig.APPLICATION_ID, walletAddress, matchEnvironment, timeFrame)
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe(disposable -> showLoadingView())
            .doOnSuccess(disposable -> showRecyclerView())
            .subscribe(this::updateRankingsList, throwable -> {
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

  private void updateRankingsList(GeneralPlayerStatsResponse generalPlayerStatsResponse) {
    List<RankingsItem> items = new ArrayList<>();
    items.add(new RankingsTitle(getString(R.string.rankings_top_3_title)));
    items.addAll(mapPlayers(generalPlayerStatsResponse.getTop3(),
        generalPlayerStatsResponse.getPlayer()
            .getRankingWalletAddress()));
    if (generalPlayerStatsResponse.getPlayer()
        .getRankingScore() > 0) {
      items.add(new RankingsTitle(getString(R.string.rankings_your_rank_title)));
      items.addAll(mapPlayers(generalPlayerStatsResponse.getAboveUser(),
          generalPlayerStatsResponse.getPlayer()
              .getRankingWalletAddress()));
      items.add(new UserRankingsItem(generalPlayerStatsResponse.getPlayer()
          .getRankingUsername(), generalPlayerStatsResponse.getPlayer()
          .getRankingScore(), generalPlayerStatsResponse.getPlayer()
          .getRankPosition(), true));
    }
    items.addAll(mapPlayers(generalPlayerStatsResponse.getBelowUser(),
        generalPlayerStatsResponse.getPlayer()
            .getRankingWalletAddress()));
    adapter.setRankings(items);
  }

  private List<UserRankingsItem> mapPlayers(UserRankings[] players, String rankingWalletAddress) {
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
