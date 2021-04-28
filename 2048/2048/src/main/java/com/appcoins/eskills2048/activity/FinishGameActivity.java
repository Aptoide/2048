package com.appcoins.eskills2048.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.appcoins.eskills2048.EmojiUtils;
import com.appcoins.eskills2048.LaunchActivity;
import com.appcoins.eskills2048.R;
import com.appcoins.eskills2048.databinding.ActivityFinishGameBinding;
import com.appcoins.eskills2048.factory.RoomApiFactory;
import com.appcoins.eskills2048.model.User;
import com.appcoins.eskills2048.repository.RoomRepository;
import com.appcoins.eskills2048.usecase.GetRoomUseCase;
import com.appcoins.eskills2048.vm.FinishGameActivityViewModel;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class FinishGameActivity extends AppCompatActivity {

  public static final String SESSION = "SESSION";
  public static final String WALLET_ADDRESS = "WALLET_ADDRESS";

  private ActivityFinishGameBinding binding;
  private FinishGameActivityViewModel viewModel;
  private final static int PARTY_POPPER_EMOJI_UNICODE = 0x1F389;
  private final static int PENSIVE_FACE_EMOJI_UNICODE = 0x1F614;

  public static final Intent buildIntent(Context context, String session, String walletAddress) {
    Intent intent = new Intent(context, FinishGameActivity.class);
    intent.putExtra(SESSION, session);
    intent.putExtra(WALLET_ADDRESS, walletAddress);
    return intent;
  }

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = ActivityFinishGameBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());

    String session = getIntent().getStringExtra(SESSION);
    viewModel = new FinishGameActivityViewModel(
        new GetRoomUseCase(new RoomRepository(RoomApiFactory.buildRoomApi())), session);

    binding.restartButton.setOnClickListener(view -> {
      Intent intent = new Intent(this, LaunchActivity.class);
      intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
      startActivity(intent);
    });

    viewModel.isWinner()
        .doOnSuccess(isWinner -> viewModel.getOpponent()
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess(opponentInfo -> setWinner(isWinner, opponentInfo))
            .subscribe())
        .doOnError(this::showErrorMessage)
        .subscribe();
  }

  private void setWinner(Boolean winner, User opponentInfo) {
    binding.lottieAnimation.setAnimation(R.raw.transact_credits_successful);
    binding.lottieAnimation.playAnimation();

    setDescriptionText(winner);
    increaseCardSize();

    String opponentDetails = getResources().getString(
        R.string.opponent_details, opponentInfo.getUserName(), opponentInfo.getScore());
    binding.opponentDetailsText.setText(opponentDetails);
    binding.opponentDetailsText.setVisibility(View.VISIBLE);
    binding.restartButton.setEnabled(true);
  }

  private void setDescriptionText(Boolean winner) {
    if (winner) {
      String partyEmoji = EmojiUtils.getEmojiByUnicode(PARTY_POPPER_EMOJI_UNICODE);
      String descriptionText = getResources().getString(R.string.you_won, partyEmoji);
      binding.animationDescriptionText.setText(descriptionText);
    } else {
      String sadEmoji = EmojiUtils.getEmojiByUnicode(PENSIVE_FACE_EMOJI_UNICODE);
      String descriptionText = getResources().getString(R.string.you_lost, sadEmoji);
      binding.animationDescriptionText.setText(descriptionText);
    }
  }

  private void increaseCardSize() {
    int cardHeight = (int) getResources()
        .getDimension(R.dimen.finish_card_max_height);
    ViewGroup.LayoutParams params = binding.card.getLayoutParams();
    params.height = cardHeight;
    binding.card.setLayoutParams(params);
    binding.getRoot().invalidate();
  }

  private void showErrorMessage(Throwable throwable) {
    throwable.printStackTrace();
    binding.lottieAnimation.setAnimation(R.raw.error_animation);
    binding.lottieAnimation.playAnimation();
    binding.animationDescriptionText.setText(getResources().getString(R.string.unknown_error));
    binding.restartButton.setEnabled(true);
  }
}
