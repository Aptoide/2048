package com.appcoins.eskills2048.repository;

import com.appcoins.eskills2048.Tile;
import com.appcoins.eskills2048.model.LocalGameStatus;
import com.appcoins.eskills2048.util.GameFieldConverter;
import com.appcoins.eskills2048.util.UserDataStorage;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class LocalGameStatusRepository {
  private static final String GAME_STATUS_PREFIX = "PREFERENCES_GAME_STATUS_";
  private static final String SESSION = "SESSION";
  private static final String WALLET_ADDRESS = "WALLET_ADDRESS";
  private static final String FIELD = "FIELD";
  private static final String SCORE = "SCORE";

  private final UserDataStorage userDataStorage;
  private final GameFieldConverter gameFieldConverter;

  @Inject public LocalGameStatusRepository(UserDataStorage userDataStorage,
      GameFieldConverter gameFieldConverter) {
    this.userDataStorage = userDataStorage;
    this.gameFieldConverter = gameFieldConverter;
  }

  public LocalGameStatus getGameStatus() {
    String session = userDataStorage.getString(GAME_STATUS_PREFIX + SESSION);
    if (session.isEmpty()) {
      return null;
    }

    String walletAddress = userDataStorage.getString(GAME_STATUS_PREFIX + WALLET_ADDRESS);
    Tile[][] field =
        gameFieldConverter.toField(userDataStorage.getString(GAME_STATUS_PREFIX + FIELD));
    long score = userDataStorage.getLong(GAME_STATUS_PREFIX + SCORE);
    removeLocalGameStatus();
    return new LocalGameStatus(session, walletAddress, field, score);
  }

  public void setGameStatus(LocalGameStatus localGameStatus) {
    userDataStorage.putString(GAME_STATUS_PREFIX + SESSION, localGameStatus.getSession());
    userDataStorage.putString(GAME_STATUS_PREFIX + WALLET_ADDRESS,
        localGameStatus.getWalletAddress());
    userDataStorage.putString(GAME_STATUS_PREFIX + FIELD,
        gameFieldConverter.fromField(localGameStatus.getField()));
    userDataStorage.putLong(GAME_STATUS_PREFIX + SCORE, localGameStatus.getScore());
  }

  private void removeLocalGameStatus() {
    userDataStorage.remove(GAME_STATUS_PREFIX + SESSION);
    userDataStorage.remove(GAME_STATUS_PREFIX + FIELD);
    userDataStorage.remove(GAME_STATUS_PREFIX + SCORE);
  }
}
