package com.appcoins.eskills2048;

import android.content.Context;

import android.widget.Toast;
import androidx.annotation.NonNull;
import com.appcoins.eskills2048.activity.FinishGameActivity;
import com.appcoins.eskills2048.model.LocalGameStatus;
import com.appcoins.eskills2048.model.RoomApiMapper;
import com.appcoins.eskills2048.model.RoomResponse;
import com.appcoins.eskills2048.model.RoomResponseErrorCode;
import com.appcoins.eskills2048.model.RoomStatus;
import com.appcoins.eskills2048.model.User;
import com.appcoins.eskills2048.model.UserDetailsHelper;
import com.appcoins.eskills2048.model.UserStatus;
import com.appcoins.eskills2048.util.UserDataStorage;
import com.appcoins.eskills2048.vm.MainGameViewModel;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MainGame {

  public static final int SPAWN_ANIMATION = -1;
  public static final int MOVE_ANIMATION = 0;
  public static final int MERGE_ANIMATION = 1;


  private static final int xorCode = 12345; // USE A DIFFERENT XOR VALUE THAN THIS!
  private static final int scoreCheckXorCode = 56789; // USE A DIFFERENT XOR VALUE THAN THIS!

  public static final int FADE_GLOBAL_ANIMATION = 0;
  private static final long MOVE_ANIMATION_TIME = MainView.BASE_ANIMATION_TIME;
  private static final long SPAWN_ANIMATION_TIME = MainView.BASE_ANIMATION_TIME;
  private static final long NOTIFICATION_DELAY_TIME = MOVE_ANIMATION_TIME + SPAWN_ANIMATION_TIME;
  private static final long NOTIFICATION_ANIMATION_TIME = MainView.BASE_ANIMATION_TIME * 5;
  private static final int startingMaxValue = 2048;
  //Odd state = game is not active
  //Even state = game is active
  //Win state = active state + 1
  private static final int GAME_WIN = 1;
  private static final int GAME_LOST = -1;
  private static final int GAME_NORMAL = 0;
  public int gameState = GAME_NORMAL;
  public int lastGameState = GAME_NORMAL;
  private int bufferGameState = GAME_NORMAL;
  private static final int GAME_ENDLESS = 2;
  private static final int GAME_ENDLESS_WON = 3;
  private static int endingMaxValue;
  final int numSquaresX = 4;
  final int numSquaresY = 4;
  private final Context mContext;
  private final MainView mView;
  private final MainGameViewModel viewModel;
  private final CompositeDisposable disposable;
  public Grid grid = null;
  public AnimationGrid aGrid;
  public boolean canUndo;
  public long score = 0;
  public long scoreCheck = 0;
  public long highScore = 0;
  public long lastScore = 0;
  public int opponentRank = 1;
  public String opponentStatus = UserStatus.PLAYING.toString();
  public String opponentName = "loading...";
  private long bufferScore = 0;
  private boolean playing = true;
  private static final int MAX_CHAR_DISPLAY_USERNAME = 11;
  private final UserDetailsHelper userDetailsHelper;

  // shared preferences related
  private final UserDataStorage userDataStorage;
  private static final String FIRST_RUN = "first run";
  private static final String HIGH_SCORE = "high score";

  public MainGame(Context context, MainView view, MainGameViewModel viewModel,
      UserDetailsHelper userDetailsHelper, UserDataStorage userDataStorage) {
    mContext = context;
    mView = view;
    endingMaxValue = (int) Math.pow(2, view.numCellTypes - 1);
    this.viewModel = viewModel;
    this.disposable = new CompositeDisposable();
    this.userDetailsHelper = userDetailsHelper;
    this.userDataStorage = userDataStorage;
  }

  public void newGame() {
    if (grid == null) {
      createOrRestoreGrid();
    } else {
      prepareUndoState();
      saveUndoState();
      grid.clearGrid();
    }
    highScore = getHighScore();
    long currentScore = getScore();
    if (currentScore >= highScore) {
      highScore = currentScore;
      recordHighScore();
    }
    gameState = GAME_NORMAL;
    mView.showHelp = firstRun();
    mView.refreshLastTime = true;
    mView.resyncTime();
    mView.invalidate();
  }

  private void createOrRestoreGrid() {
    aGrid = new AnimationGrid(numSquaresX, numSquaresY);
    LocalGameStatus gameStatus = viewModel.getGameStatus();
    if (gameStatus == null) {
      grid = new Grid(numSquaresX, numSquaresY);
      score = 0;
      score = score ^ xorCode;
      scoreCheck = 0;
      scoreCheck = scoreCheck ^ scoreCheckXorCode;
      addStartTiles();
    } else {
      grid = new Grid(gameStatus.getField());
      score = gameStatus.getScore();
    }
  }

  public long getScore(){
    return score^xorCode ;
  }
  private void addStartTiles() {
    int startTiles = 2;
    for (int xx = 0; xx < startTiles; xx++) {
      addRandomTile();
    }
  }

  private void addRandomTile() {
    if (grid.isCellsAvailable()) {
      int value = Math.random() < 0.9 ? 2 : 4;
      Tile tile = new Tile(grid.randomAvailableCell(), value);
      spawnTile(tile);
    }
  }

  private void spawnTile(Tile tile) {
    grid.insertTile(tile);
    aGrid.startAnimation(tile.getX(), tile.getY(), SPAWN_ANIMATION, SPAWN_ANIMATION_TIME,
        MOVE_ANIMATION_TIME, null); //Direction: -1 = EXPANDING
  }

  private void recordHighScore() {
    userDataStorage.putLong(HIGH_SCORE, highScore);
  }

  private long getHighScore() {
    return userDataStorage.getLong(HIGH_SCORE);
  }

  private boolean firstRun() {
    if (userDataStorage.getBoolean(FIRST_RUN)) {
      userDataStorage.putBoolean(FIRST_RUN, false);
      return true;
    }
    return false;
  }

  private void prepareTiles() {
    for (Tile[] array : grid.field) {
      for (Tile tile : array) {
        if (grid.isCellOccupied(tile)) {
          tile.setMergedFrom(null);
        }
      }
    }
  }

  private void moveTile(Tile tile, Cell cell) {
    grid.field[tile.getX()][tile.getY()] = null;
    grid.field[cell.getX()][cell.getY()] = tile;
    tile.updatePosition(cell);
  }

  private void saveUndoState() {
    grid.saveTiles();
    canUndo = true;
    lastScore = bufferScore;
    lastGameState = bufferGameState;
  }

  private void prepareUndoState() {
    grid.prepareSaveTiles();
    bufferScore = score;
    bufferGameState = gameState;
  }

  public void revertUndoState() {
    if (canUndo) {
      canUndo = false;
      aGrid.cancelAnimations();
      grid.revertTiles();
      score = lastScore;
      scoreCheck = lastScore^xorCode;
      scoreCheck ^= scoreCheckXorCode;
      disposable.add(viewModel.setScore(getScore())
          .subscribe(this::onSuccess, Throwable::printStackTrace));
      gameState = lastGameState;
      mView.refreshLastTime = true;
      mView.invalidate();
    }
  }

  private void onSuccess(RoomResponse roomResponse) {
    if (roomResponse.getStatusCode()
        .equals(RoomResponse.StatusCode.REGION_NOT_SUPPORTED)) {
      endGame(false,roomResponse.getStatusCode());
    }
  }

  public boolean gameWon() {
    return (gameState > 0 && gameState % 2 != 0);
  }

  public boolean gameLost() {
    return (gameState == GAME_LOST);
  }

  public boolean isActive() {
    return !(gameWon() || gameLost());
  }

  public void move(int direction) {
    aGrid.cancelAnimations();
    // 0: up, 1: right, 2: down, 3: left
    if (!isActive()) {
      return;
    }
    prepareUndoState();
    Cell vector = getVector(direction);
    List<Integer> traversalsX = buildTraversalsX(vector);
    List<Integer> traversalsY = buildTraversalsY(vector);
    boolean moved = false;

    prepareTiles();

    for (int xx : traversalsX) {
      for (int yy : traversalsY) {
        Cell cell = new Cell(xx, yy);
        Tile tile = grid.getCellContent(cell);

        if (tile != null) {
          Cell[] positions = findFarthestPosition(cell, vector);
          Tile next = grid.getCellContent(positions[1]);

          if (next != null && next.getValue() == tile.getValue() && next.getMergedFrom() == null) {
            Tile merged = new Tile(positions[1], tile.getValue() * 2);
            Tile[] temp = { tile, next };
            merged.setMergedFrom(temp);

            grid.insertTile(merged);
            grid.removeTile(tile);

            // Converge the two tiles' positions
            tile.updatePosition(positions[1]);

            int[] extras = { xx, yy };
            aGrid.startAnimation(merged.getX(), merged.getY(), MOVE_ANIMATION, MOVE_ANIMATION_TIME,
                0, extras); //Direction: 0 = MOVING MERGED
            aGrid.startAnimation(merged.getX(), merged.getY(), MERGE_ANIMATION,
                SPAWN_ANIMATION_TIME, MOVE_ANIMATION_TIME, null);

            // Update the score
            score ^= xorCode;
            score = score + merged.getValue();
            score ^= xorCode;
            scoreCheck ^= scoreCheckXorCode;
            scoreCheck = scoreCheck + merged.getValue();
            scoreCheck ^= scoreCheckXorCode;
            disposable.add(viewModel.setScore(getScore())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onSuccess, Throwable::printStackTrace));
            highScore = Math.max(getScore(), highScore);

            // The mighty 2048 tile
            if (merged.getValue() >= winValue() && !gameWon()) {
              gameState = gameState + GAME_WIN; // Set win state
              endGame();
            }
          } else {
            moveTile(tile, positions[0]);
            int[] extras = { xx, yy, 0 };
            aGrid.startAnimation(positions[0].getX(), positions[0].getY(), MOVE_ANIMATION,
                MOVE_ANIMATION_TIME, 0, extras); //Direction: 1 = MOVING NO MERGE
          }

          if (!positionsEqual(cell, tile)) {
            moved = true;
          }
        }
      }
    }

    if (moved) {
      saveUndoState();
      addRandomTile();
      checkLose();
    }

    viewModel.setGameStatus(grid.field, score);
    mView.resyncTime();
    mView.invalidate();
  }

  private void checkLose() {
    if (!movesAvailable() && !gameWon()) {
      gameState = GAME_LOST;
      endGame();
    }
  }

  private void endGame() {
    endGame(true, RoomResponse.StatusCode.SUCCESSFUL_RESPONSE);
  }

  public void endGame(boolean setFinalScore, RoomResponse.StatusCode statusCode) {
    playing = false;
    aGrid.startAnimation(-1, -1, FADE_GLOBAL_ANIMATION, NOTIFICATION_ANIMATION_TIME,
        NOTIFICATION_DELAY_TIME, null);
    long currentScore = getScore();
    if (currentScore >= highScore) {
      highScore = currentScore;
      recordHighScore();
    }
    if (setFinalScore) {
      disposable.add(viewModel.setFinalScore(getScore())
          .subscribe(roomResponse -> {
          }, Throwable::printStackTrace));
    }

    mContext.startActivity(FinishGameActivity.buildIntent(
        mContext,
        viewModel.getSession(),
        viewModel.getWalletAddress(),
        viewModel.getMatchEnvironment(),
        score,
        statusCode
    ));
  }

  private Cell getVector(int direction) {
    Cell[] map = {
        new Cell(0, -1), // up
        new Cell(1, 0),  // right
        new Cell(0, 1),  // down
        new Cell(-1, 0)  // left
    };
    return map[direction];
  }

  private List<Integer> buildTraversalsX(Cell vector) {
    List<Integer> traversals = new ArrayList<>();

    for (int xx = 0; xx < numSquaresX; xx++) {
      traversals.add(xx);
    }
    if (vector.getX() == 1) {
      Collections.reverse(traversals);
    }

    return traversals;
  }

  private List<Integer> buildTraversalsY(Cell vector) {
    List<Integer> traversals = new ArrayList<>();

    for (int xx = 0; xx < numSquaresY; xx++) {
      traversals.add(xx);
    }
    if (vector.getY() == 1) {
      Collections.reverse(traversals);
    }

    return traversals;
  }

  private Cell[] findFarthestPosition(Cell cell, Cell vector) {
    Cell previous;
    Cell nextCell = new Cell(cell.getX(), cell.getY());
    do {
      previous = nextCell;
      nextCell = new Cell(previous.getX() + vector.getX(), previous.getY() + vector.getY());
    } while (grid.isCellWithinBounds(nextCell) && grid.isCellAvailable(nextCell));

    return new Cell[] { previous, nextCell };
  }

  private boolean movesAvailable() {
    return grid.isCellsAvailable() || tileMatchesAvailable();
  }

  private boolean tileMatchesAvailable() {
    Tile tile;

    for (int xx = 0; xx < numSquaresX; xx++) {
      for (int yy = 0; yy < numSquaresY; yy++) {
        tile = grid.getCellContent(new Cell(xx, yy));

        if (tile != null) {
          for (int direction = 0; direction < 4; direction++) {
            Cell vector = getVector(direction);
            Cell cell = new Cell(xx + vector.getX(), yy + vector.getY());

            Tile other = grid.getCellContent(cell);

            if (other != null && other.getValue() == tile.getValue()) {
              return true;
            }
          }
        }
      }
    }

    return false;
  }

  private boolean positionsEqual(Cell first, Cell second) {
    return first.getX() == second.getX() && first.getY() == second.getY();
  }

  private int winValue() {
    if (!canContinue()) {
      return endingMaxValue;
    } else {
      return startingMaxValue;
    }
  }

  public void setEndlessMode() {
    gameState = GAME_ENDLESS;
    mView.invalidate();
    mView.refreshLastTime = true;
  }

  public boolean canContinue() {
    return !(gameState == GAME_ENDLESS || gameState == GAME_ENDLESS_WON);
  }

  public void stop() {
    disposable.clear();
  }

  public void resume() {
    startPeriodicOpponentUpdate();
  }

  private void startPeriodicOpponentUpdate() {
    disposable.add(Observable.interval(0, 3L, TimeUnit.SECONDS)
        .flatMapSingle(aLong -> viewModel.getRoom()
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess(MainGame.this::updateInfo)
            .doOnError(Throwable::printStackTrace)
            .onErrorReturnItem(new RoomResponse()))
        .takeWhile(roomResponse -> playing)
        .subscribe());
  }

  private boolean confirmScoreValidity() {
    return (score ^ xorCode) == (scoreCheck ^ scoreCheckXorCode);
  }

  private void updateInfo(RoomResponse roomResponse) {
    if (!confirmScoreValidity()){
      disposable.add(viewModel.setFinalScore(-1)
          .subscribe(this::onSuccess, Throwable::printStackTrace));
    }
    if (roomResponse.getStatus() == RoomStatus.COMPLETED
        || roomResponse.getCurrentUser()
        .getStatus() == UserStatus.TIME_UP) {
      endGame(false, RoomResponse.StatusCode.SUCCESSFUL_RESPONSE);
    }
    // if match environment is set to sandbox, the number of opponents can be 0
    try {
      List<User> opponents = roomResponse.getOpponents(viewModel.getWalletAddress());
      User opponent = userDetailsHelper.getNextOpponent(opponents);
      viewModel.notify(opponent);
      opponentRank = roomResponse.getUserRank(opponent);
      opponentStatus = opponent.getStatus().toString();
      opponentName = truncate(opponent.getUserName(), MAX_CHAR_DISPLAY_USERNAME);
      mView.invalidate();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public String truncate(String str, int len) {
    if (str.length() > len) {
      return str.substring(0, len) + "...";
    } else {
      return str;
    }
  }

  public void onOpponentFinished(User opponent) {
    Toast.makeText(mView.getContext(), "Opponent " + opponent.getUserName() + " has finished.",
        Toast.LENGTH_LONG)
        .show();
  }
}
