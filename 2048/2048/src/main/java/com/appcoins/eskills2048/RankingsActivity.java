package com.appcoins.eskills2048;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.appcoins.eskills2048.api.GeneralPlayerStats;
import com.appcoins.eskills2048.model.GeneralPlayerStatsResponse;
import com.appcoins.eskills2048.model.RankingsItem;
import com.appcoins.eskills2048.model.RankingsTitle;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RankingsActivity extends AppCompatActivity {

  private RecyclerView recyclerView;
  private List<RankingsItem> items;
  private static final String WALLET_ADDRESS_KEY = "WALLET_ADDRESS_KEY";

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

    recyclerView = findViewById(R.id.rankingsRecyclerView);
    items = new ArrayList<>();

    Retrofit retrofit =
        new Retrofit.Builder().baseUrl("https://api.eskills.dev.catappult.io/room/statistics/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build();

    GeneralPlayerStats generalPlayerStats = retrofit.create(GeneralPlayerStats.class);
    Disposable subscribe =
        generalPlayerStats.getGeneralPlayerStats(BuildConfig.APPLICATION_ID, userWalletAddress)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::updateRankingsList, Throwable::printStackTrace);
  }

  private void updateRankingsList(GeneralPlayerStatsResponse generalPlayerStatsResponse) {
    items.add(new RankingsTitle("TOP 3"));
    items.addAll(Arrays.asList(generalPlayerStatsResponse.getTop3()));
    items.add(new RankingsTitle("Your rank"));
    items.addAll(Arrays.asList(generalPlayerStatsResponse.getAboveUser()));
    items.add(generalPlayerStatsResponse.getPlayer());
    items.addAll(Arrays.asList(generalPlayerStatsResponse.getBelowUser()));
    putDataIntoRecyclerView(items);
  }

  private void putDataIntoRecyclerView(List<RankingsItem> items) {
    RankingsAdapter adapter = new RankingsAdapter(this, items);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    recyclerView.setAdapter(adapter);
  }
}