package com.appcoins.eskills2048.bonus;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.appcoins.eskills2048.BuildConfig;
import com.appcoins.eskills2048.R;
import com.appcoins.eskills2048.model.BonusHistory;
import com.appcoins.eskills2048.model.BonusRankingsItem;
import com.appcoins.eskills2048.model.BonusUser;
import com.appcoins.eskills2048.model.RankingsItem;
import com.appcoins.eskills2048.model.RankingsTitle;
import com.appcoins.eskills2048.repository.StatisticsTimeFrame;
import com.appcoins.eskills2048.usecase.GetBonusHistoryUseCase;
import com.appcoins.eskills2048.usecase.GetNextBonusScheduleUseCase;
import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

@AndroidEntryPoint public class BonusContentFragment extends Fragment {

  private static final long COUNTDOWN_INTERVAL = 1000;

  private static final String TIME_FRAME_KEY = "TIME_FRAME_KEY";

  private static final String SKU_KEY = "SKU_KEY";
  private StatisticsTimeFrame timeFrame;
  private String sku;
  private BonusAdapter adapter;
  private final CompositeDisposable disposables = new CompositeDisposable();
  private View loadingView;
  private RecyclerView recyclerView;
  private View errorView;
  private View countdownView;

  @Inject GetBonusHistoryUseCase getBonusHistoryUseCase;
  @Inject GetNextBonusScheduleUseCase getNextBonusScheduleUseCase;

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
      sku = (String) arguments.getSerializable(SKU_KEY);
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
    countdownView = view.findViewById(R.id.countdown_timer);
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

  private void showTime() {
    if (timeFrame == StatisticsTimeFrame.TODAY) {
      countdownView.findViewById(R.id.countdown_days_container)
          .setVisibility(View.GONE);
    }

    TextView daysView = countdownView.findViewById(R.id.countdown_days);
    TextView hoursView = countdownView.findViewById(R.id.countdown_hours);
    TextView minutesView = countdownView.findViewById(R.id.countdown_minutes);
    TextView secondsView = countdownView.findViewById(R.id.countdown_seconds);
    disposables.add(getNextBonusScheduleUseCase.execute(timeFrame)
        .observeOn(AndroidSchedulers.mainThread())
        .doOnSuccess(nextSchedule -> new CountDownTimer(
            nextSchedule.getNextSchedule() - System.currentTimeMillis(), COUNTDOWN_INTERVAL) {
          @Override public void onTick(long millisUntilFinished) {
            long seconds = millisUntilFinished / 1000;
            long minutes = seconds / 60;
            long hours = minutes / 60;
            long days = hours / 24;

            daysView.setText((int) days);
            hoursView.setText((int) hours);
            minutesView.setText((int) minutes);
            secondsView.setText((int) seconds);
          }

          @Override public void onFinish() {
            showTime();   // update schedule
          }
        }.start())
        .subscribe());
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

  private void updateBonusList(List<BonusHistory> bonusHistory) {
    BonusHistory currentBonus = bonusHistory.get(0);
    List<RankingsItem> items = new ArrayList<>();
    items.add(new RankingsTitle(currentBonus.getDate()));
    items.addAll(mapPlayers(currentBonus.getUsers()));
    adapter.setRankings(items);
  }

  private List<BonusRankingsItem> mapPlayers(List<BonusUser> players) {
    // TODO
    ArrayList<BonusRankingsItem> playersList = new ArrayList<>();
    for (BonusUser player : players) {
      playersList.add(
          new BonusRankingsItem(player.getUserName(), player.getScore(), player.getRank(),
              player.getBonusAmount(), false));
    }
    return playersList;
  }

  @Override public void onDestroyView() {
    disposables.clear();
    super.onDestroyView();
  }
}
