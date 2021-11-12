package com.appcoins.eskills2048.model;

import com.appcoins.eskills2048.Tile;
import java.io.Serializable;

public class LocalGameStatus implements Serializable {
  private final String session;
  private final String walletAddress;
  private final Tile[][] field;
  private final long score;

  public LocalGameStatus(String session, String walletAddress, Tile[][] field, long score) {
    this.session = session;
    this.walletAddress = walletAddress;
    this.field = field;
    this.score = score;
  }

  public String getSession() {
    return session;
  }

  public String getWalletAddress() {
    return walletAddress;
  }

  public Tile[][] getField() {
    return field;
  }

  public long getScore() {
    return score;
  }
}
