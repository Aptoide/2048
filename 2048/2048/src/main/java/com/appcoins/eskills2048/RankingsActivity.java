package com.appcoins.eskills2048;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appcoins.eskills2048.api.GeneralPlayerStats;
import com.appcoins.eskills2048.model.GeneralPlayerStatsResponse;
import com.appcoins.eskills2048.model.RankingsItem;
import com.appcoins.eskills2048.model.RankingsTitle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RankingsActivity extends AppCompatActivity {

    private final String TAG = "RankingsActivity";
    RecyclerView recyclerView;
    List<RankingsItem> items;
    private String hardCodedWalletAddress = "0x05512a1c8457380898181ef0f02e4c752200c6c5";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rankings);

        recyclerView = findViewById(R.id.rankingsRecyclerView);
        items = new ArrayList<>();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.eskills.dev.catappult.io/room/statistics/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        GeneralPlayerStats generalPlayerStats = retrofit.create(GeneralPlayerStats.class);
        Call<GeneralPlayerStatsResponse> call = generalPlayerStats.getGeneralPlayerStats(
                BuildConfig.APPLICATION_ID, hardCodedWalletAddress);


        call.enqueue(new Callback<GeneralPlayerStatsResponse>() {
            @Override
            public void onResponse(Call<GeneralPlayerStatsResponse> call, Response<GeneralPlayerStatsResponse> response) {
                Log.d(TAG, "Api request response arrived.");
                GeneralPlayerStatsResponse generalPlayerStatsResponse = response.body();
                assert response.body() != null;
                Log.d(TAG, response.body().toString());
                assert generalPlayerStatsResponse != null;
                items.add(new RankingsTitle("TOP 3"));
                items.addAll(Arrays.asList(generalPlayerStatsResponse.getTop3()));
                items.add(new RankingsTitle("Your rank"));
                items.addAll(Arrays.asList(generalPlayerStatsResponse.getAboveUser()));
                items.add(generalPlayerStatsResponse.getPlayer());
                items.addAll(Arrays.asList(generalPlayerStatsResponse.getBelowUser()));
                PutDataIntoRecyclerView(items);
            }

            @Override
            public void onFailure(Call<GeneralPlayerStatsResponse> call, Throwable t) {
                Log.d(TAG, t.toString());
                Log.d(TAG, call.request().toString());
                Log.d(TAG, "Api request failed");
            }
        });
    }

    private void PutDataIntoRecyclerView(List<RankingsItem> items) {
        RankingsAdapter adapter = new RankingsAdapter(this, items);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
}