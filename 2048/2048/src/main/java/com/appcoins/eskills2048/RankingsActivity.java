package com.appcoins.eskills2048;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.appcoins.eskills2048.api.GeneralPlayerStats;
import com.appcoins.eskills2048.model.GeneralPlayerStatsResponse;
import com.appcoins.eskills2048.model.UserRankings;

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
    RecyclerView topScoresRecyclerView;
    RecyclerView playerPositionRecyclerView;
    List<UserRankings> topScoresList;
    List<UserRankings> playerRankList;
    private String hardCodedWalletAddress = "0xb114e6753c66547be9ece10b447fbaa9ec06e523";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rankings);

        topScoresRecyclerView = findViewById(R.id.topScoresRecyclerView);
        playerPositionRecyclerView = findViewById(R.id.playerPositionRecyclerView);
        playerRankList = new ArrayList<>();

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
                Log.d(TAG,"Api request response arrived.");
                GeneralPlayerStatsResponse generalPlayerStatsResponse = response.body();
                assert response.body() != null;
                Log.d(TAG, response.body().toString());
                assert generalPlayerStatsResponse != null;
                topScoresList = new ArrayList<>(Arrays.asList(generalPlayerStatsResponse.getTop3()));
                playerRankList.addAll(Arrays.asList(generalPlayerStatsResponse.getAboveUser()));
                playerRankList.add(generalPlayerStatsResponse.getPlayer());
                playerRankList.addAll(Arrays.asList(generalPlayerStatsResponse.getBelowUser()));
                PutDataIntoRecyclerView(topScoresList, topScoresRecyclerView);
                PutDataIntoRecyclerView(playerRankList, playerPositionRecyclerView);
            }

            @Override
            public void onFailure(Call<GeneralPlayerStatsResponse> call, Throwable t) {
                Log.d(TAG, t.toString());
                Log.d(TAG, call.request().toString());
                Log.d(TAG,"Api request failed");
            }
        });
    }

    private void PutDataIntoRecyclerView(List<UserRankings> players, RecyclerView recyclerView){
        RankingsAdapter adapter = new RankingsAdapter(this, players);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
}