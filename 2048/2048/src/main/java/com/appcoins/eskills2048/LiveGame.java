package com.appcoins.eskills2048;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;
import com.appcoins.eskills2048.model.LocalGameStatus;
import com.appcoins.eskills2048.model.RoomResponse;
import com.appcoins.eskills2048.model.RoomStatus;
import com.appcoins.eskills2048.model.ScoreHandler;
import com.appcoins.eskills2048.model.User;
import com.appcoins.eskills2048.model.UserDetailsHelper;
import com.appcoins.eskills2048.model.UserStatus;
import com.appcoins.eskills2048.util.UserDataStorage;
import com.appcoins.eskills2048.vm.MainGameViewModel;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class LiveGame extends MainGame {

  private final UserDetailsHelper userDetailsHelper;
  private final CompositeDisposable disposable;

  private final MainGameViewModel viewModel;
  public int opponentRank = 1;
  public String opponentStatus = UserStatus.PLAYING.toString();
  public String opponentName = "loading...";

  private boolean playing = true;
  private static final int MAX_CHAR_DISPLAY_USERNAME = 11;

  public LiveGame(MainView view, MainGameViewModel viewModel, UserDetailsHelper userDetailsHelper,
      UserDataStorage userDataStorage, ScoreHandler scoreHandler) {
    super(view, userDataStorage, scoreHandler);
    this.viewModel = viewModel;
    this.userDetailsHelper = userDetailsHelper;
    this.disposable = new CompositeDisposable();
  }

  @Override protected void createGrid() {
    aGrid = new AnimationGrid(numSquaresX, numSquaresY);
    LocalGameStatus gameStatus = viewModel.getGameStatus();
    if (gameStatus == null) {
      super.createGrid();
    } else {
      grid = new Grid(gameStatus.getField());
      Log.d("RESTART_ERROR", "gameStatus score:" + gameStatus.getScore());
      scoreHandler.initScore(gameStatus.getScore());
    }
  }

  @Override public void revertUndoState() {
    if (canUndo) {
      disposable.add(viewModel.setScore(scoreHandler.getScore())
          .subscribe(this::onSuccess, Throwable::printStackTrace));
    }
    super.revertUndoState();
  }

  private void onSuccess(RoomResponse roomResponse) {
    if (roomResponse.getStatusCode()
        .equals(RoomResponse.StatusCode.REGION_NOT_SUPPORTED)) {
      endGame(false);
    }
  }

  @Override public void move(int direction) {
    super.move(direction);
    viewModel.setGameStatus(grid.field, scoreHandler.getScore());
  }

  @Override protected void updateScore(int score) {
    super.updateScore(score);
    disposable.add(viewModel.setScore(scoreHandler.getScore())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(this::onSuccess, Throwable::printStackTrace));
  }

  @Override public void endGame(boolean setFinalScore) {
    playing = false;
    if (setFinalScore) {
      disposable.add(viewModel.setFinalScore(scoreHandler.getScore())
          .subscribe(roomResponse -> {
          }, Throwable::printStackTrace));
    }
    // handle end of game
    Toast.makeText(mView.getContext(), "Exiting...", Toast.LENGTH_SHORT)
        .show();
    mView.postDelayed(() -> {
      mView.getContext()
          .startActivity(new Intent(mView.getContext(), LaunchActivity.class));
      ((Activity) mView.getContext()).finish();
    }, 2000);
  }

  @Override public void stop() {
    disposable.clear();
  }

  @Override public void resume() {
    startPeriodicOpponentUpdate();
  }

  private void startPeriodicOpponentUpdate() {
    disposable.add(Observable.interval(0, 3L, TimeUnit.SECONDS)
        .flatMapSingle(aLong -> viewModel.getRoom()
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess(LiveGame.this::updateInfo)
            .doOnError(Throwable::printStackTrace)
            .onErrorReturnItem(new RoomResponse()))
        .takeWhile(roomResponse -> playing)
        .subscribe());
  }

  private void updateInfo(RoomResponse roomResponse) {
    if (!scoreHandler.confirmScoreValidity()) {
      disposable.add(viewModel.setFinalScore(-1)
          .subscribe(this::onSuccess, Throwable::printStackTrace));
    }
    if (roomResponse.getStatus() == RoomStatus.COMPLETED
        || roomResponse.getCurrentUser()
        .getStatus() == UserStatus.TIME_UP) {
      endGame(false);
    }
    // if match environment is set to sandbox, the number of opponents can be 0
    try {
      List<User> opponents = roomResponse.getOpponents(viewModel.getWalletAddress());
      User opponent = userDetailsHelper.getNextOpponent(opponents);
      viewModel.notify(opponent);
      opponentRank = roomResponse.getUserRank(opponent);
      opponentStatus = opponent.getStatus()
          .toString();
      opponentName = truncate(opponent.getUserName(), MAX_CHAR_DISPLAY_USERNAME);
      mView.invalidate();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void onOpponentFinished(User opponent) {
    Toast.makeText(mView.getContext(), "Opponent " + opponent.getUserName() + " has finished.",
            Toast.LENGTH_LONG)
        .show();
  }

  private String truncate(String str, int len) {
    if (str.length() > len) {
      return str.substring(0, len) + "...";
    } else {
      return str;
    }
  }
}
