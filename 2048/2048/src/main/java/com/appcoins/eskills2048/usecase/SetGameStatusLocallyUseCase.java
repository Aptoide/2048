package com.appcoins.eskills2048.usecase;

import com.appcoins.eskills2048.model.LocalGameStatus;
import com.appcoins.eskills2048.repository.LocalGameStatusRepository;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class SetGameStatusLocallyUseCase {
  private final LocalGameStatusRepository localGameStatusRepository;

  @Inject public SetGameStatusLocallyUseCase(LocalGameStatusRepository localGameStatusRepository) {
    this.localGameStatusRepository = localGameStatusRepository;
  }

  public void setGameStatus(LocalGameStatus localGameStatus) {
    localGameStatusRepository.setGameStatus(localGameStatus);
  }
}
