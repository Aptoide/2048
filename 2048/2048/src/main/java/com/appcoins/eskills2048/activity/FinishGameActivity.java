package com.appcoins.eskills2048.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.appcoins.eskills2048.LaunchActivity;
import com.appcoins.eskills2048.PlayerRankingAdapter;
import com.appcoins.eskills2048.R;
import com.appcoins.eskills2048.bonus.BonusActivity;
import com.appcoins.eskills2048.databinding.ActivityFinishGameBinding;
import com.appcoins.eskills2048.model.MatchDetails;
import com.appcoins.eskills2048.model.RoomResponse;
import com.appcoins.eskills2048.model.RoomResult;
import com.appcoins.eskills2048.model.RoomStatus;
import com.appcoins.eskills2048.model.User;
import com.appcoins.eskills2048.model.UserStatus;
import com.appcoins.eskills2048.rankins.RankingsActivity;
import com.appcoins.eskills2048.repository.LocalGameStatusRepository;
import com.appcoins.eskills2048.usecase.GetRoomUseCase;
import com.appcoins.eskills2048.usecase.SetFinalScoreUseCase;
import com.appcoins.eskills2048.util.DeviceScreenManager;
import com.appcoins.eskills2048.util.EmojiUtils;
import com.appcoins.eskills2048.vm.FinishGameActivityViewModel;
import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;

@AndroidEntryPoint public class FinishGameActivity extends AppCompatActivity {

  public static final String SESSION = "SESSION";
  public static final String WALLET_ADDRESS = "WALLET_ADDRESS";
  public static final String USER_SCORE = "USER_SCORE";
  private static final String STATUS_CODE = "STATUS_CODE";
  private static final Long GET_ROOM_PERIOD_SECONDS = 3L;
  private static final String MATCH_ENVIRONMENT = "MATCH_ENVIRONMENT";

  private ActivityFinishGameBinding binding;
  private FinishGameActivityViewModel viewModel;
  private final static int PARTY_POPPER_EMOJI_UNICODE = 0x1F389;
  private final static int PENSIVE_FACE_EMOJI_UNICODE = 0x1F614;
  private final static int ALARM_CLOCK_EMOJI_UNICODE = 0x23F0;
  private CompositeDisposable disposables;
  private RecyclerView recyclerView;
  private PlayerRankingAdapter adapter;

  @Inject GetRoomUseCase getRoomUseCase;
  @Inject SetFinalScoreUseCase setFinalScoreUseCase;
  @Inject LocalGameStatusRepository localGameStatusRepository;

  public static Intent buildIntent(Context context, String session, String walletAddress,
      MatchDetails.Environment matchEnvironment, long score, RoomResponse.StatusCode statusCode) {
    Intent intent = new Intent(context, FinishGameActivity.class);
    intent.putExtra(SESSION, session);
    intent.putExtra(WALLET_ADDRESS, walletAddress);
    intent.putExtra(MATCH_ENVIRONMENT, matchEnvironment);
    intent.putExtra(USER_SCORE, score);
    intent.putExtra(STATUS_CODE, statusCode);
    return intent;
  }

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = ActivityFinishGameBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());
    DeviceScreenManager.keepAwake(getWindow());
    buildRecyclerView();
    localGameStatusRepository.removeLocalGameStatus();

    disposables = new CompositeDisposable();
    Intent intent = getIntent();
    String session = intent.getStringExtra(SESSION);
    String walletAddress = intent.getStringExtra(WALLET_ADDRESS);
    MatchDetails.Environment matchEnvironment =
        (MatchDetails.Environment) intent.getSerializableExtra(MATCH_ENVIRONMENT);
    long userScore = intent.getLongExtra(USER_SCORE, -1);
    viewModel = new FinishGameActivityViewModel(getRoomUseCase, setFinalScoreUseCase, session,
        walletAddress, userScore);

    disposables.add(Observable.interval(0, GET_ROOM_PERIOD_SECONDS, TimeUnit.SECONDS)
        .flatMapSingle(aLong -> viewModel.getRoom()
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess(FinishGameActivity.this::updateRecyclerView)
            .doOnError(Throwable::printStackTrace)
            .onErrorReturnItem(new RoomResponse()))
        .takeUntil(roomResponse -> roomResponse.getStatus() == RoomStatus.COMPLETED)
        .subscribe());

    binding.restartButton.setOnClickListener(view -> {
      DeviceScreenManager.stopKeepAwake(getWindow());
      Intent restartIntent = new Intent(this, LaunchActivity.class);
      restartIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
      startActivity(restartIntent);
    });

    if (this.getIntent()
        .getSerializableExtra(STATUS_CODE) == RoomResponse.StatusCode.REGION_NOT_SUPPORTED) {
      binding.geofencingErrorMessage.setVisibility(View.VISIBLE);
      showErrorMessage();
      binding.retryButton.setVisibility(View.GONE);
      binding.rankingsButton.setVisibility(View.INVISIBLE);
      binding.restartButton.setEnabled(true);
      binding.restartButton.setVisibility(View.VISIBLE);
    } else {
      binding.retryButton.setOnClickListener(v -> disposables.add(viewModel.getRoomResult()
          .subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())
          .doOnSubscribe(disposable -> showLoading())
          .doOnSuccess(this::setRoomResultDetails)
          .doOnError(this::showErrorMessage)
          .subscribe(roomResult -> {
          }, Throwable::printStackTrace)));

      disposables.add(viewModel.getRoomResult()
          .subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())
          .doOnSuccess(this::setRoomResultDetails)
          .doOnError(this::showErrorMessage)
          .subscribe(roomResult -> {
          }, Throwable::printStackTrace));
      findViewById(R.id.rankings_button).setOnClickListener(
          view -> startActivity(RankingsActivity.create(this, walletAddress, "1v1", matchEnvironment)));
      findViewById(R.id.rankings_rewards_button).setOnClickListener(
          view -> startActivity(BonusActivity.create(this, "1v1")));
    }
  }

  private void buildRecyclerView() {
    recyclerView = findViewById(R.id.ranking_recycler_view);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    adapter = new PlayerRankingAdapter(new ArrayList<>());
    recyclerView.setAdapter(adapter);
  }

  private void showLoading() {
    binding.lottieAnimation.setAnimation(R.raw.transact_loading_animation);
    binding.lottieAnimation.playAnimation();
    binding.animationDescriptionText.setText(R.string.waiting_for_opponents_to_finish);
    binding.restartButton.setEnabled(false);
    binding.restartButton.setVisibility(View.VISIBLE);
    binding.retryButton.setVisibility(View.GONE);
  }

  private void updateRecyclerView(RoomResponse roomResponse) {
    adapter.updateData(roomResponse.getUsersSortedByScore());
  }

  private void setRoomResultDetails(RoomResponse room) {
    recyclerView.setVisibility(View.GONE);
    binding.lottieAnimation.setAnimation(R.raw.transact_credits_successful);
    binding.lottieAnimation.playAnimation();
    if (viewModel.isWinner(room.getRoomResult())) {
      handleRoomWinnerBehaviour(room.getRoomResult());
    } else {
      handleRoomLoserBehaviour(room);
    }
    binding.restartButton.setEnabled(true);
    binding.restartButton.setVisibility(View.VISIBLE);
    binding.retryButton.setVisibility(View.GONE);
  }

  private void handleRoomWinnerBehaviour(RoomResult roomResult) {
    String partyEmoji = EmojiUtils.getEmojiByUnicode(PARTY_POPPER_EMOJI_UNICODE);
    String descriptionText = getResources().getString(R.string.you_won, partyEmoji);
    binding.animationDescriptionText.setText(descriptionText);

    String opponentDetails =
        getResources().getString(R.string.amount_won_details, roomResult.getWinnerAmount());
    binding.secondaryMessage.setText(opponentDetails);
    binding.secondaryMessage.setVisibility(View.VISIBLE);
  }

  private void handleRoomLoserBehaviour(RoomResponse roomResponse) {
    String sadEmoji = EmojiUtils.getEmojiByUnicode(PENSIVE_FACE_EMOJI_UNICODE);
    String alarmEmoji = EmojiUtils.getEmojiByUnicode(ALARM_CLOCK_EMOJI_UNICODE);
    String descriptionText;
    if (roomResponse.getCurrentUser()
        .getStatus() == UserStatus.TIME_UP) {
      descriptionText = getResources().getString(R.string.you_lost_timeout, alarmEmoji);
    } else {
      descriptionText = getResources().getString(R.string.you_lost, sadEmoji);
    }
    binding.animationDescriptionText.setText(descriptionText);

    User winner = roomResponse.getRoomResult()
        .getWinner();
    String opponentDetails =
        getResources().getString(R.string.opponent_details, winner.getUserName(),
            winner.getScore());
    binding.secondaryMessage.setText(opponentDetails);
    binding.secondaryMessage.setVisibility(View.VISIBLE);
  }

  private void showErrorMessage(Throwable throwable) {
    showErrorMessage();
    throwable.printStackTrace();
  }

  private void showErrorMessage() {
    binding.lottieAnimation.setAnimation(R.raw.error_animation);
    binding.lottieAnimation.playAnimation();
    binding.animationDescriptionText.setText(getResources().getString(R.string.unknown_error));
    binding.retryButton.setVisibility(View.VISIBLE);
    binding.restartButton.setVisibility(View.GONE);
    binding.restartButton.setEnabled(true);
  }
}
