package com.appcoins.eskills2048.vm;

import com.appcoins.eskills2048.model.LocalGameStatus;
import com.appcoins.eskills2048.model.MatchDetails;

public class MainGameViewModelData {

  private final String userId;
  private final String walletAddress;
  private final MatchDetails.Environment matchEnvironment;
  private final String session;
  private final LocalGameStatus localGameStatus;

  public MainGameViewModelData(String userId, String walletAddress, MatchDetails.Environment matchEnvironment,
                               String session, LocalGameStatus localGameStatus) {
    this.userId = userId;
    this.walletAddress = walletAddress;
    this.matchEnvironment = matchEnvironment;
    this.session = session;
    this.localGameStatus = localGameStatus;
  }

  public String getWalletAddress() {
    return walletAddress;
  }

  public MatchDetails.Environment getMatchEnvironment() {return matchEnvironment;}

  public String getSession() {
    return session;
  }

  public LocalGameStatus getLocalGameStatus() {
    return localGameStatus;
  }
}
