package com.appcoins.eskills2048.rankins;

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
import com.appcoins.eskills2048.databinding.CountdownTimerLayoutBinding;
import com.appcoins.eskills2048.databinding.CurrentTop3Binding;
import com.appcoins.eskills2048.databinding.FirstRowLayoutBinding;
import com.appcoins.eskills2048.databinding.FragmentRankingsTabContentBinding;
import com.appcoins.eskills2048.databinding.SecondRowLayoutBinding;
import com.appcoins.eskills2048.databinding.ThirdRowLayoutBinding;
import com.appcoins.eskills2048.model.BonusHistory;
import com.appcoins.eskills2048.model.MatchDetails;
import com.appcoins.eskills2048.model.RankingsItem;
import com.appcoins.eskills2048.model.TopRankings;
import com.appcoins.eskills2048.model.UserRankingsItem;
import com.appcoins.eskills2048.repository.StatisticsTimeFrame;
import com.appcoins.eskills2048.usecase.GetBonusHistoryUseCase;
import com.appcoins.eskills2048.usecase.GetNextBonusScheduleUseCase;
import com.appcoins.eskills2048.usecase.GetUserStatisticsUseCase;
import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;

@AndroidEntryPoint public class RankingsContentFragment extends Fragment {
  private static final String WALLET_ADDRESS_KEY = "WALLET_ADDRESS_KEY";
  private static final String TIME_FRAME_KEY = "TIME_FRAME_KEY";
  private static final String MATCH_ENVIRONMENT = "MATCH_ENVIRONMENT";
  private static final String SKU_KEY = "SKU_KEY";
  private static final long COUNTDOWN_INTERVAL = 1000;

  private StatisticsTimeFrame timeFrame;
  private MatchDetails.Environment matchEnvironment;
  private String walletAddress;
  private String sku;
  private RankingsAdapter adapter;
  private final CompositeDisposable disposables = new CompositeDisposable();
  private View loadingView;
  private RecyclerView recyclerView;
  private View errorView;
  private CurrentTop3Binding currentTop3View;
  private CountdownTimerLayoutBinding countdownBinding;
  private CountDownTimer countDownTimer;

  private FragmentRankingsTabContentBinding binding;

  @Inject GetUserStatisticsUseCase getUserStatisticsUseCase;
  @Inject GetBonusHistoryUseCase getBonusHistoryUseCase;
  @Inject GetNextBonusScheduleUseCase getNextBonusScheduleUseCase;

  public static RankingsContentFragment newInstance(String walletAddress, String sku,
      MatchDetails.Environment matchEnvironment, StatisticsTimeFrame timeFrame) {
    Bundle args = new Bundle();
    args.putString(WALLET_ADDRESS_KEY, walletAddress);
    args.putSerializable(MATCH_ENVIRONMENT, matchEnvironment);
    args.putSerializable(TIME_FRAME_KEY, timeFrame);
    args.putSerializable(SKU_KEY, sku);
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
      sku = arguments.getString(SKU_KEY);
    }
  }

  @Nullable @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    binding = FragmentRankingsTabContentBinding.inflate(inflater, container, false);
    return binding.getRoot();
  }

  @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    recyclerView = binding.rankingsRecyclerView;
    adapter = new RankingsAdapter(LayoutInflater.from(getContext()));
    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    recyclerView.setAdapter(adapter);
    currentTop3View = binding.currentTop3Container;
    countdownBinding = binding.currentTop3Container.countdownTimerContainer;
    loadingView = binding.loading;
    errorView = binding.errorView;
    showRankings();

    if(timeFrame == StatisticsTimeFrame.ALL_TIME) {
      binding.lastWinnersContainer.getRoot().setVisibility(View.GONE);
      binding.currentTop3Container.getRoot().setVisibility(View.GONE);
    }
    else {
      showLastBonusWinners();
      showCountdownTimer();
    }
    binding.retryButton.setOnClickListener(view1 -> showRankings());
  }

  private void showRankings() {
    disposables.add(getUserStatisticsUseCase.execute(BuildConfig.APPLICATION_ID, walletAddress,
            matchEnvironment, timeFrame)
        .observeOn(AndroidSchedulers.mainThread())
        .doOnSubscribe(disposable -> showLoadingView())
        .doOnSuccess(disposable -> showRecyclerView())
        .subscribe(topRankings -> {
          updateCurrentRanking(topRankings.getCurrentUser());
          updateRankingsList(processTop3(topRankings.getUserList()));
        }, throwable -> {
          throwable.printStackTrace();
          showErrorView();
        }));
  }

  private void showLastBonusWinners() {
    disposables.add(getBonusHistoryUseCase.execute(BuildConfig.APPLICATION_ID, sku, timeFrame)
        .observeOn(AndroidSchedulers.mainThread())
        .doOnSubscribe(disposable -> showLoadingView())
        .doOnSuccess(disposable -> showRecyclerView())
        .subscribe(this::updateLastBonusWinners, throwable -> {
          throwable.printStackTrace();
          showErrorView();
        }));
  }

  // process top 3 and return the original list minus that 3 players
  private TopRankings[] processTop3(TopRankings[] players_score) {
    if (players_score.length < 3 || timeFrame == StatisticsTimeFrame.ALL_TIME) {
      currentTop3View.getRoot()
          .setVisibility(View.GONE);
      return players_score;
    }
    TopRankings player1 = players_score[0];
    TopRankings player2 = players_score[1];
    TopRankings player3 = players_score[2];
    FirstRowLayoutBinding firstPlayerRowBinding = binding.currentTop3Container.firstPlayerRow;
    SecondRowLayoutBinding secondPlayerRowBinding = binding.currentTop3Container.secondPlayerRow;
    ThirdRowLayoutBinding thirdRowLayoutBinding = binding.currentTop3Container.thirdPlayerRow;

    populateTop3row(player1, firstPlayerRowBinding.rankingUsername,
        firstPlayerRowBinding.rankingScore);
    populateTop3row(player2, secondPlayerRowBinding.rankingUsername,
        secondPlayerRowBinding.rankingScore);
    populateTop3row(player3, thirdRowLayoutBinding.rankingUsername,
        thirdRowLayoutBinding.rankingScore);

    return Arrays.copyOfRange(players_score, 3, players_score.length);
  }

  private void populateTop3row(TopRankings player, TextView username, TextView score) {
    username.setText(player.getUsername());
    score.setText(String.valueOf(player.getScore()));
  }

  private void showCountdownTimer() {
    if (timeFrame == StatisticsTimeFrame.TODAY) {
      countdownBinding.countdownDaysContainer.setVisibility(View.GONE);
    }
    disposables.add(getNextBonusScheduleUseCase.execute(timeFrame)
        .observeOn(AndroidSchedulers.mainThread())
        .doOnSuccess(nextSchedule -> {
          long timeLeftMillis = nextSchedule.getNextSchedule() * 1000 - System.currentTimeMillis();
          startCountDownTimer(timeLeftMillis);
        })
        .subscribe());
  }

  private void updateLastBonusWinners(List<BonusHistory> bonusHistoryList) {
    // TODO
  }

  private void updateCurrentRanking(TopRankings currentRanking) {
    binding.currentRankingContainer.rankingScore.setText(String.valueOf(currentRanking.getScore()));
    binding.currentRankingContainer.rankingPosition.setText(
        String.valueOf(currentRanking.getRankPosition()));
  }

  private void startCountDownTimer(long timeLeftMillis) {
    TextView daysView = countdownBinding.countdownDays;
    TextView hoursView = countdownBinding.countdownHours;
    TextView minutesView = countdownBinding.countdownMinutes;
    TextView secondsView = countdownBinding.countdownSeconds;
    countDownTimer = new CountDownTimer(timeLeftMillis, COUNTDOWN_INTERVAL) {
      @Override public void onTick(long millisUntilFinished) {
        long days = TimeUnit.MILLISECONDS.toDays(millisUntilFinished);
        long hours = TimeUnit.MILLISECONDS.toHours(millisUntilFinished) % 24;
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60;
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60;

        daysView.setText(String.valueOf(days));
        hoursView.setText(String.valueOf(hours));
        minutesView.setText(String.valueOf(minutes));
        secondsView.setText(String.valueOf(seconds));
      }

      @Override public void onFinish() {
        showCountdownTimer();   // update schedule
      }
    }.start();
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

  private void updateRankingsList(TopRankings[] rankings) {
    List<RankingsItem> items = new ArrayList<>(mapPlayers(rankings));
    adapter.setRankings(items);
  }

  private List<UserRankingsItem> mapPlayers(TopRankings[] players) {
    ArrayList<UserRankingsItem> playersList = new ArrayList<>();
    for (TopRankings player : players) {
      playersList.add(
          new UserRankingsItem(player.getUsername(), player.getScore(), player.getRankPosition(),
              false)
      );
    }
    return playersList;
  }

  @Override public void onDestroyView() {
    disposables.clear();
    if (countDownTimer != null) {
      countDownTimer.cancel();
    }
    super.onDestroyView();
    binding = null;
  }
}
