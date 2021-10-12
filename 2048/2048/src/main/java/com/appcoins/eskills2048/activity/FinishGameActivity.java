package com.appcoins.eskills2048.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.appcoins.eskills2048.EmojiUtils;
import com.appcoins.eskills2048.LaunchActivity;
import com.appcoins.eskills2048.R;
import com.appcoins.eskills2048.databinding.ActivityFinishGameBinding;
import com.appcoins.eskills2048.factory.RoomApiFactory;
import com.appcoins.eskills2048.model.RoomResult;
import com.appcoins.eskills2048.rankins.RankingsActivity;
import com.appcoins.eskills2048.repository.RoomRepository;
import com.appcoins.eskills2048.usecase.GetRoomUseCase;
import com.appcoins.eskills2048.usecase.SetFinalScoreUseCase;
import com.appcoins.eskills2048.util.DeviceScreenManager;
import com.appcoins.eskills2048.vm.FinishGameActivityViewModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class FinishGameActivity extends AppCompatActivity {

  public static final String SESSION = "SESSION";
  public static final String WALLET_ADDRESS = "WALLET_ADDRESS";
  public static final String USER_SCORE = "USER_SCORE";

  private ActivityFinishGameBinding binding;
  private FinishGameActivityViewModel viewModel;
  private final static int PARTY_POPPER_EMOJI_UNICODE = 0x1F389;
  private final static int PENSIVE_FACE_EMOJI_UNICODE = 0x1F614;
  private CompositeDisposable disposables;

  public static Intent buildIntent(Context context, String session, String walletAddress,
      long score) {
    Intent intent = new Intent(context, FinishGameActivity.class);
    intent.putExtra(SESSION, session);
    intent.putExtra(WALLET_ADDRESS, walletAddress);
    intent.putExtra(USER_SCORE, score);
    return intent;
  }

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = ActivityFinishGameBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());
    DeviceScreenManager.keepAwake(getWindow());

    disposables = new CompositeDisposable();
    String session = getIntent().getStringExtra(SESSION);
    String walletAddress = getIntent().getStringExtra(WALLET_ADDRESS);
    long userScore = getIntent().getLongExtra(USER_SCORE, -1);
    RoomRepository roomRepository = new RoomRepository(RoomApiFactory.buildRoomApi());
    viewModel = new FinishGameActivityViewModel(new GetRoomUseCase(roomRepository),
        new SetFinalScoreUseCase(roomRepository), session, walletAddress, userScore);

    binding.restartButton.setOnClickListener(view -> {
      DeviceScreenManager.stopKeepAwake(getWindow());
      Intent intent = new Intent(this, LaunchActivity.class);
      intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
      startActivity(intent);
    });

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
        view -> startActivity(RankingsActivity.create(this, walletAddress)));
  }

  private void showLoading() {
    binding.lottieAnimation.setAnimation(R.raw.transact_loading_animation);
    binding.lottieAnimation.playAnimation();
    binding.animationDescriptionText.setText(R.string.waiting_for_opponents_to_finish);
    binding.restartButton.setEnabled(false);
    binding.restartButton.setVisibility(View.VISIBLE);
    binding.retryButton.setVisibility(View.GONE);
  }

  private void setRoomResultDetails(RoomResult roomResult) {
    binding.lottieAnimation.setAnimation(R.raw.transact_credits_successful);
    binding.lottieAnimation.playAnimation();

    if (viewModel.isWinner(roomResult)) {
      handleRoomWinnerBehaviour(roomResult);
    } else {
      handleRoomLoserBehaviour(roomResult);
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

  private void handleRoomLoserBehaviour(RoomResult roomResult) {
    String sadEmoji = EmojiUtils.getEmojiByUnicode(PENSIVE_FACE_EMOJI_UNICODE);
    String descriptionText = getResources().getString(R.string.you_lost, sadEmoji);
    binding.animationDescriptionText.setText(descriptionText);

    String opponentDetails = getResources().getString(R.string.opponent_details,
        roomResult.getWinner()
            .getUserName(), roomResult.getWinner()
            .getScore());
    binding.secondaryMessage.setText(opponentDetails);
    binding.secondaryMessage.setVisibility(View.VISIBLE);
  }

  private void showErrorMessage(Throwable throwable) {
    throwable.printStackTrace();
    binding.lottieAnimation.setAnimation(R.raw.error_animation);
    binding.lottieAnimation.playAnimation();
    binding.animationDescriptionText.setText(getResources().getString(R.string.unknown_error));
    binding.retryButton.setVisibility(View.VISIBLE);
    binding.restartButton.setVisibility(View.GONE);
    binding.restartButton.setEnabled(true);
  }
}
