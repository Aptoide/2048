package com.appcoins.eskills2048;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import androidx.appcompat.app.AppCompatActivity;
import com.appcoins.eskills2048.factory.RoomApiFactory;
import com.appcoins.eskills2048.model.LocalGameStatus;
import com.appcoins.eskills2048.model.UserDetailsHelper;
import com.appcoins.eskills2048.repository.LocalGameStatusRepository;
import com.appcoins.eskills2048.repository.RoomRepository;
import com.appcoins.eskills2048.usecase.GetRoomUseCase;
import com.appcoins.eskills2048.usecase.SetFinalScoreUseCase;
import com.appcoins.eskills2048.usecase.SetGameStatusLocallyUseCase;
import com.appcoins.eskills2048.usecase.SetScoreUseCase;
import com.appcoins.eskills2048.util.GameFieldConverter;
import com.appcoins.eskills2048.util.UserDataStorage;
import com.appcoins.eskills2048.vm.MainGameViewModel;
import com.appcoins.eskills2048.vm.MainGameViewModelData;
import com.google.gson.Gson;

import static com.appcoins.eskills2048.LaunchActivity.LOCAL_GAME_STATUS;
import static com.appcoins.eskills2048.LaunchActivity.SESSION;
import static com.appcoins.eskills2048.LaunchActivity.SHARED_PREFERENCES_NAME;
import static com.appcoins.eskills2048.LaunchActivity.USER_ID;
import static com.appcoins.eskills2048.LaunchActivity.WALLET_ADDRESS;

public class MainActivity extends AppCompatActivity {
  private MainView view;

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
    RoomRepository roomRepository = new RoomRepository(RoomApiFactory.buildRoomApi());
    UserDataStorage userDataStorage = new UserDataStorage(this, SHARED_PREFERENCES_NAME);
    LocalGameStatusRepository localGameStatusRepository =
        new LocalGameStatusRepository(userDataStorage, new GameFieldConverter(new Gson()));
    view = new MainView(this, new MainGameViewModel(new SetScoreUseCase(roomRepository),
        new SetFinalScoreUseCase(roomRepository), buildViewModelData(),
        new GetRoomUseCase(roomRepository),
        new SetGameStatusLocallyUseCase(localGameStatusRepository)), new UserDetailsHelper(),
        userDataStorage);

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
