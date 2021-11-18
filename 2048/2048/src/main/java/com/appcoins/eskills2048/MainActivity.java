package com.appcoins.eskills2048;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import androidx.appcompat.app.AppCompatActivity;
import com.appcoins.eskills2048.model.LocalGameStatus;
import com.appcoins.eskills2048.model.User;
import com.appcoins.eskills2048.model.UserDetailsHelper;
import com.appcoins.eskills2048.usecase.GetRoomUseCase;
import com.appcoins.eskills2048.usecase.NotifyOpponentFinishedUseCase;
import com.appcoins.eskills2048.usecase.SetFinalScoreUseCase;
import com.appcoins.eskills2048.usecase.SetGameStatusLocallyUseCase;
import com.appcoins.eskills2048.usecase.SetScoreUseCase;
import com.appcoins.eskills2048.util.UserDataStorage;
import com.appcoins.eskills2048.vm.MainGameViewModel;
import com.appcoins.eskills2048.vm.MainGameViewModelData;
import dagger.hilt.android.AndroidEntryPoint;
import javax.inject.Inject;

import static com.appcoins.eskills2048.LaunchActivity.LOCAL_GAME_STATUS;
import static com.appcoins.eskills2048.LaunchActivity.SESSION;
import static com.appcoins.eskills2048.LaunchActivity.USER_ID;
import static com.appcoins.eskills2048.LaunchActivity.WALLET_ADDRESS;

@AndroidEntryPoint public class MainActivity extends AppCompatActivity
    implements OpponentStatusListener {
  private MainView view;

  @Inject UserDataStorage userDataStorage;
  @Inject GetRoomUseCase getRoomUseCase;
  @Inject SetScoreUseCase setScoreUseCase;
  @Inject SetFinalScoreUseCase setFinalScoreUseCase;
  @Inject SetGameStatusLocallyUseCase setGameStatusLocallyUseCase;
  @Inject UserDetailsHelper userDetailsHelper;

  public static Intent newIntent(Context context, String userId, String walletAddress,
      String session, LocalGameStatus localGameStatus) {
    Intent intent = new Intent(context, MainActivity.class);
    intent.putExtra(USER_ID, userId);
    intent.putExtra(WALLET_ADDRESS, walletAddress);
    intent.putExtra(SESSION, session);
    intent.putExtra(LOCAL_GAME_STATUS, localGameStatus);
    return intent;
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    view = new MainView(this,
        new MainGameViewModel(setScoreUseCase, setFinalScoreUseCase, buildViewModelData(),
            getRoomUseCase, setGameStatusLocallyUseCase, new NotifyOpponentFinishedUseCase(this)),
        userDetailsHelper, userDataStorage);
    setContentView(view);
  }

  private MainGameViewModelData buildViewModelData() {
    String userId = getIntent().getStringExtra(USER_ID);
    String walletAddress = getIntent().getStringExtra(WALLET_ADDRESS);
    String session = getIntent().getStringExtra(SESSION);
    LocalGameStatus localGameStatus =
        (LocalGameStatus) getIntent().getSerializableExtra(LOCAL_GAME_STATUS);

    return new MainGameViewModelData(userId, walletAddress, session, localGameStatus);
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

  @Override public void onOpponentFinished(User opponent) {
    view.game.onOpponentFinished(opponent);
  }
}
