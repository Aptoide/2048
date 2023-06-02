package com.appcoins.eskills2048.model;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton public class ScoreHandler {
  private long xorCode = 12345; // USE A DIFFERENT XOR VALUE THAN THIS!
  private long scoreCheckXorCode = 56789; // USE A DIFFERENT XOR VALUE THAN THIS!
  private long score;
  private long scoreCheck;

  @Inject public ScoreHandler() {
    initScore(0);
  }

  public void setScore(long value) {
    score = score ^ xorCode;
    score += value;
    score = score ^ xorCode;
    scoreCheck = scoreCheck ^ scoreCheckXorCode;
    scoreCheck += value;
    scoreCheck = scoreCheck ^ scoreCheckXorCode;
  }

  public void initScore(long initialScore) {
    score = initialScore;
    score = score ^ xorCode;
    scoreCheck = initialScore;
    scoreCheck = scoreCheck ^ scoreCheckXorCode;
  }

  public long getScore() {
    return score ^ xorCode;
  }

  public boolean confirmScoreValidity() {
    return (score ^ xorCode) == (scoreCheck ^ scoreCheckXorCode);
  }
}
