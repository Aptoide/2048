package com.appcoins.eskills2048;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import androidx.appcompat.app.AppCompatActivity;
import com.appcoins.eskills2048.model.LocalGameStatus;
import com.appcoins.eskills2048.model.MatchDetails;
import com.appcoins.eskills2048.model.UserDetailsHelper;
import com.appcoins.eskills2048.usecase.GetRoomUseCase;
import com.appcoins.eskills2048.usecase.SetFinalScoreUseCase;
import com.appcoins.eskills2048.usecase.SetGameStatusLocallyUseCase;
import com.appcoins.eskills2048.usecase.SetScoreUseCase;
import com.appcoins.eskills2048.util.UserDataStorage;
import com.appcoins.eskills2048.vm.MainGameViewModel;
import com.appcoins.eskills2048.vm.MainGameViewModelData;
import dagger.hilt.android.AndroidEntryPoint;
import javax.inject.Inject;

import static com.appcoins.eskills2048.LaunchActivity.LOCAL_GAME_STATUS;
import static com.appcoins.eskills2048.LaunchActivity.MATCH_ENVIRONMENT;
import static com.appcoins.eskills2048.LaunchActivity.SESSION;
import static com.appcoins.eskills2048.LaunchActivity.USER_ID;
import static com.appcoins.eskills2048.LaunchActivity.WALLET_ADDRESS;

@AndroidEntryPoint public class MainActivity extends AppCompatActivity {
  private MainView view;

  @Inject UserDataStorage userDataStorage;
  @Inject GetRoomUseCase getRoomUseCase;
  @Inject SetScoreUseCase setScoreUseCase;
  @Inject SetFinalScoreUseCase setFinalScoreUseCase;
  @Inject SetGameStatusLocallyUseCase setGameStatusLocallyUseCase;
  @Inject UserDetailsHelper userDetailsHelper;

  public static Intent newIntent(Context context, String userId, String walletAddress,
                                 MatchDetails.Environment match_environment, String session, LocalGameStatus localGameStatus) {
    Intent intent = new Intent(context, MainActivity.class);
    intent.putExtra(USER_ID, userId);
    intent.putExtra(WALLET_ADDRESS, walletAddress);
    intent.putExtra(MATCH_ENVIRONMENT, match_environment);
    intent.putExtra(SESSION, session);
    intent.putExtra(LOCAL_GAME_STATUS, localGameStatus);
    return intent;
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    view = new MainView(this,
        new MainGameViewModel(setScoreUseCase, setFinalScoreUseCase, buildViewModelData(),
            getRoomUseCase, setGameStatusLocallyUseCase), userDetailsHelper, userDataStorage);
    setContentView(view);
  }

  private MainGameViewModelData buildViewModelData() {
    String userId = getIntent().getStringExtra(USER_ID);
    String walletAddress = getIntent().getStringExtra(WALLET_ADDRESS);
    MatchDetails.Environment matchEnvironment = (MatchDetails.Environment) getIntent().getSerializableExtra(MATCH_ENVIRONMENT);
    String session = getIntent().getStringExtra(SESSION);
    LocalGameStatus localGameStatus =
        (LocalGameStatus) getIntent().getSerializableExtra(LOCAL_GAME_STATUS);

    return new MainGameViewModelData(userId, walletAddress, matchEnvironment, session, localGameStatus);
  }

  @Override protected void onResume() {
    view.onResume();
    super.onResume();
  }

  @Override protected void onPause() {
    view.onPause();
    super.onPause();
  }

  @Override public boolean onKeyDown(int keyCode, KeyEvent event) {
    if (keyCode == KeyEvent.KEYCODE_MENU) {
      //Do nothing
      return true;
    } else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
      view.game.move(2);
      return true;
    } else if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
      view.game.move(0);
      return true;
    } else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
      view.game.move(3);
      return true;
    } else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
      view.game.move(1);
      return true;
    }
    return super.onKeyDown(keyCode, event);
  }

  @Override public void onBackPressed() {
    view.onBackPressed();
  }
}
