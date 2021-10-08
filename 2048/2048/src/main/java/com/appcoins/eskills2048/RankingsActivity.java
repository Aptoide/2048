package com.appcoins.eskills2048;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.appcoins.eskills2048.api.GeneralPlayerStats;
import com.appcoins.eskills2048.factory.StatisticsApiFactory;
import com.appcoins.eskills2048.model.GeneralPlayerStatsResponse;
import com.appcoins.eskills2048.model.RankingsItem;
import com.appcoins.eskills2048.model.RankingsTitle;
import com.appcoins.eskills2048.model.UserRankings;
import com.appcoins.eskills2048.model.UserRankingsItem;
import com.appcoins.eskills2048.repository.StatisticsRepository;
import com.appcoins.eskills2048.usecase.GetUserStatisticsUseCase;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import java.util.ArrayList;
import java.util.List;

public class RankingsActivity extends AppCompatActivity {

  private static final String WALLET_ADDRESS_KEY = "WALLET_ADDRESS_KEY";
  private final CompositeDisposable disposables = new CompositeDisposable();
  private RankingsAdapter adapter;

  static public Intent create(Context context, String walletAddress) {
    Intent intent = new Intent(context, RankingsActivity.class);
    intent.putExtra(WALLET_ADDRESS_KEY, walletAddress);
    return intent;
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_rankings);
    String userWalletAddress = getIntent().getExtras()
        .getString(WALLET_ADDRESS_KEY);

    RecyclerView recyclerView = findViewById(R.id.rankingsRecyclerView);
    GeneralPlayerStats generalPlayerStats = StatisticsApiFactory.buildRoomApi();
    StatisticsRepository statisticsRepository = new StatisticsRepository(generalPlayerStats);
    GetUserStatisticsUseCase statisticsUseCase = new GetUserStatisticsUseCase(statisticsRepository);
    adapter = new RankingsAdapter(LayoutInflater.from(this));
    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    recyclerView.setAdapter(adapter);

    disposables.add(statisticsUseCase.execute(BuildConfig.APPLICATION_ID, userWalletAddress)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(this::updateRankingsList, Throwable::printStackTrace));
  }

  @Override protected void onDestroy() {
    disposables.clear();
    super.onDestroy();
  }

  private void updateRankingsList(GeneralPlayerStatsResponse generalPlayerStatsResponse) {
    List<RankingsItem> items = new ArrayList<>();
    items.add(new RankingsTitle(getString(R.string.rankings_top_3_title)));
    items.addAll(mapPlayers(generalPlayerStatsResponse.getTop3(),
        generalPlayerStatsResponse.getPlayer()
            .getRankingWalletAddress()));
    items.add(new RankingsTitle(getString(R.string.rankings_your_rank_title)));
    items.addAll(mapPlayers(generalPlayerStatsResponse.getAboveUser(),
        generalPlayerStatsResponse.getPlayer()
            .getRankingWalletAddress()));
    items.add(new UserRankingsItem(generalPlayerStatsResponse.getPlayer()
        .getRankingUsername(), generalPlayerStatsResponse.getPlayer()
        .getRankingScore(), generalPlayerStatsResponse.getPlayer()
        .getRankPosition(), true));
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
}