package com.appcoins.eskills2048;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.appcoins.eskills2048.databinding.EndgameFragmentSkillsBinding;
import com.appcoins.eskills2048.util.DeviceScreenManager;
import com.appcoins.eskills2048.util.EmojiUtils;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint public class FinishSandboxGameActivity extends AppCompatActivity {
  private final static int PARTY_POPPER_EMOJI_UNICODE = 0x1F389;
  private static String SCORE = "SCORE";
  private EndgameFragmentSkillsBinding binding;

  public static Intent buildIntent(Context context, long score) {
    Intent intent = new Intent(context, FinishSandboxGameActivity.class);
    intent.putExtra(SCORE, String.valueOf(score));
    return intent;
  }

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = EndgameFragmentSkillsBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());
    DeviceScreenManager.keepAwake(getWindow());
    handleRoomWinnerBehaviour();
    binding.restartButton.setOnClickListener(view -> {
      DeviceScreenManager.stopKeepAwake(getWindow());
      Intent restartIntent = new Intent(this, LaunchActivity.class);
      restartIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
      startActivity(restartIntent);
    });
  }
  private void handleRoomWinnerBehaviour() {
    binding.lottieAnimation.setAnimation(R.raw.transact_credits_successful);
    binding.lottieAnimation.playAnimation();
    String partyEmoji = EmojiUtils.getEmojiByUnicode(PARTY_POPPER_EMOJI_UNICODE);
    String descriptionText = getString(R.string.game_over_icon, partyEmoji);
    binding.animationDescriptionText.setText(descriptionText);
    String opponentDetails = getString(R.string.opponent_details,"You",getIntent().getStringExtra(SCORE));
    binding.secondaryMessage.setText(opponentDetails);
    binding.secondaryMessage.setVisibility(View.VISIBLE);
  }
}
