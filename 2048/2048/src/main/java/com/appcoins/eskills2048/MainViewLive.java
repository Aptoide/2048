package com.appcoins.eskills2048;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.Button;
import com.appcoins.eskills2048.model.ScoreHandler;
import com.appcoins.eskills2048.model.UserDetailsHelper;
import com.appcoins.eskills2048.usecase.GetRoomUseCase;
import com.appcoins.eskills2048.usecase.NotifyOpponentFinishedUseCase;
import com.appcoins.eskills2048.usecase.SetFinalScoreUseCase;
import com.appcoins.eskills2048.usecase.SetGameStatusLocallyUseCase;
import com.appcoins.eskills2048.usecase.SetScoreUseCase;
import com.appcoins.eskills2048.util.UserDataStorage;
import com.appcoins.eskills2048.vm.MainGameViewModel;
import com.appcoins.eskills2048.vm.MainGameViewModelData;

public class MainViewLive extends MainView {
  //Internal variables
  private int titleWidthOpponentRank;
  private int titleWidthOpponentName;
  private int titleWidthOpponentStatus;

  public MainViewLive(Context context) {
    super(context);
  }

  public MainViewLive(Context context, MainGameViewModelData mainGameViewModelData,
      UserDataStorage userDataStorage, GetRoomUseCase getRoomUseCase,
      SetScoreUseCase setScoreUseCase, SetFinalScoreUseCase setFinalScoreUseCase,
      SetGameStatusLocallyUseCase setGameStatusLocallyUseCase, UserDetailsHelper userDetailsHelper,
      ScoreHandler scoreHandler) {
    super(context, userDataStorage, scoreHandler);

    super.game = new LiveGame(this,
        new MainGameViewModel(setScoreUseCase, setFinalScoreUseCase, mainGameViewModelData,
            getRoomUseCase, setGameStatusLocallyUseCase, new NotifyOpponentFinishedUseCase(
            opponent -> ((LiveGame) game).onOpponentFinished(opponent))), userDetailsHelper,
        userDataStorage, scoreHandler);

    game.newGame();
  }

  @Override protected void getLayout(int width, int height) {

    titleWidthOpponentRank = (int) (paint.measureText(getResources().getString(R.string.rank)));
    titleWidthOpponentName = (int) (paint.measureText(getResources().getString(R.string.opponent)));
    titleWidthOpponentStatus =
        (int) (paint.measureText(getResources().getString(R.string.opponent_score)));

    super.getLayout(width, height);
  }

  @Override protected void drawScoreText(Canvas canvas) {
    super.drawScoreText(canvas);
    int bodyWidthOpponentRank = (int) (paint.measureText("" + ((LiveGame) game).opponentRank));
    int bodyWidthOpponentName = (int) (paint.measureText("" + ((LiveGame) game).opponentName));
    int bodyWidthOpponentStatus = (int) (paint.measureText("" + ((LiveGame) game).opponentStatus));

    int textWidthOpponentRank =
        Math.max(titleWidthOpponentRank, bodyWidthOpponentRank) + textPaddingSize;
    int textWidthOpponentName =
        Math.max(titleWidthOpponentName, bodyWidthOpponentName) + textPaddingSize;
    int textWidthOpponentScore =
        Math.max(titleWidthOpponentStatus, bodyWidthOpponentStatus) + textPaddingSize;

    int textMiddleOpponentRank = textWidthOpponentRank / 2;
    int textMiddleOpponentName = textWidthOpponentName / 2;
    int textMiddleOpponentScore = textWidthOpponentScore / 2;

    int sYOpponentDetails = sYIcons;
    int eYOpponentDetails = sYIcons + iconSize;

    int sXOpponentRank = startingX;
    int eXOpponentRank = sXOpponentRank + textWidthOpponentRank;

    int sXOpponentName = eXOpponentRank + textPaddingSize;
    int eXOpponentName = sXOpponentName + textWidthOpponentName;

    int sXOpponentScore = eXOpponentName + textPaddingSize;
    int eXOpponentScore = sXOpponentScore + textWidthOpponentScore;

    //Outputting opponent rank box
    backgroundRectangle.setBounds(sXOpponentRank, sYOpponentDetails, eXOpponentScore,
        eYOpponentDetails);
    backgroundRectangle.draw(canvas);

    paint.setTextSize(titleTextSize);
    paint.setColor(getResources().getColor(R.color.text_brown));
    canvas.drawText(getResources().getString(R.string.rank),
        sXOpponentRank + textMiddleOpponentRank, sYOpponentDetails + iconPaddingSize * 2, paint);
    paint.setTextSize(bodyTextSize);
    paint.setColor(getResources().getColor(R.color.text_white));
    canvas.drawText(String.valueOf(((LiveGame) game).opponentRank),
        sXOpponentRank + textMiddleOpponentRank, sYOpponentDetails + iconPaddingSize * 5, paint);

    //Outputting opponent name box
    paint.setTextSize(titleTextSize);
    paint.setColor(getResources().getColor(R.color.text_brown));
    canvas.drawText(getResources().getString(R.string.opponent),
        sXOpponentName + textMiddleOpponentName, sYOpponentDetails + iconPaddingSize * 2, paint);
    paint.setTextSize(bodyTextSize);
    paint.setColor(getResources().getColor(R.color.text_white));
    canvas.drawText(String.valueOf(((LiveGame) game).opponentName),
        sXOpponentName + textMiddleOpponentName, sYOpponentDetails + iconPaddingSize * 5, paint);

    //Outputting opponent score box
    paint.setTextSize(titleTextSize);
    paint.setColor(getResources().getColor(R.color.text_brown));
    canvas.drawText(getResources().getString(R.string.opponent_score),
        sXOpponentScore + textMiddleOpponentScore, sYOpponentDetails + iconPaddingSize * 2, paint);
    paint.setTextSize(bodyTextSize);
    paint.setColor(getResources().getColor(R.color.text_white));
    canvas.drawText(String.valueOf(((LiveGame) game).opponentStatus),
        sXOpponentScore + textMiddleOpponentScore, sYOpponentDetails + iconPaddingSize * 5, paint);
  }

  public void onBackPressed() {
    showQuitGameDialog();
  }

  private void showQuitGameDialog() {
    Dialog dialog = new Dialog(getContext());
    dialog.getWindow()
        .setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    View view = View.inflate(getContext(), R.layout.quit_confirmation_layout, null);
    dialog.setContentView(view);

    Button cancelButton = view.findViewById(R.id.back_button);
    cancelButton.setOnClickListener(v -> dialog.dismiss());

    Button quitGameButton = view.findViewById(R.id.quit_game_button);
    quitGameButton.setOnClickListener(v -> {
      handleQuitGame();
      dialog.dismiss();
    });
    dialog.show();
  }

  private void handleQuitGame() {
    game.endGame(true);
  }

  public void onResume() {
    game.resume();
  }
}
