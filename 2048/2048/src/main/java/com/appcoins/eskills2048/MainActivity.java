package com.appcoins.eskills2048;

import android.os.Bundle;
import android.view.KeyEvent;

import androidx.appcompat.app.AppCompatActivity;

import com.appcoins.eskills2048.factory.RoomApiFactory;
import com.appcoins.eskills2048.model.UserDetailsHelper;
import com.appcoins.eskills2048.repository.RoomRepository;
import com.appcoins.eskills2048.usecase.GetRoomUseCase;
import com.appcoins.eskills2048.usecase.SetFinalScoreUseCase;
import com.appcoins.eskills2048.usecase.SetScoreUseCase;
import com.appcoins.eskills2048.vm.MainGameViewModel;
import com.appcoins.eskills2048.vm.MainGameViewModelData;

public class MainActivity extends AppCompatActivity {

    private static final String WIDTH = "width";
    private static final String HEIGHT = "height";
    private static final String SCORE = "score";
    private static final String HIGH_SCORE = "high score temp";
    private static final String UNDO_SCORE = "undo score";
    private static final String CAN_UNDO = "can undo";
    private static final String UNDO_GRID = "undo";
    private static final String GAME_STATE = "game state";
    private static final String UNDO_GAME_STATE = "undo game state";
    private MainView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RoomRepository roomRepository = new RoomRepository(RoomApiFactory.buildRoomApi());
        view = new MainView(this, new MainGameViewModel(new SetScoreUseCase(roomRepository),
                new SetFinalScoreUseCase(roomRepository), buildViewModelData(),
                new GetRoomUseCase(roomRepository)), new UserDetailsHelper());

        setContentView(view);
    }

    private MainGameViewModelData buildViewModelData() {
        String roomId = getIntent().getStringExtra(LaunchActivity.ROOM_ID);
        String userId = getIntent().getStringExtra(LaunchActivity.USER_ID);
        String walletAddress = getIntent().getStringExtra(LaunchActivity.WALLET_ADDRESS);
        String session = getIntent().getStringExtra(LaunchActivity.SESSION);

        return new MainGameViewModelData(userId, walletAddress, session);
    }

    @Override
    protected void onDestroy() {
        view.onDestroy();
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
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
}
