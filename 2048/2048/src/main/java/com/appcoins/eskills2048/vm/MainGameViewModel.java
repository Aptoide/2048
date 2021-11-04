package com.appcoins.eskills2048.vm;

import com.appcoins.eskills2048.model.LocalGameStatus;
import com.appcoins.eskills2048.Tile;
import com.appcoins.eskills2048.model.RoomResponse;
import com.appcoins.eskills2048.usecase.GetGameStatusLocallyUseCase;
import com.appcoins.eskills2048.usecase.GetRoomUseCase;
import com.appcoins.eskills2048.usecase.SetFinalScoreUseCase;
import com.appcoins.eskills2048.usecase.SetGameStatusLocallyUseCase;
import com.appcoins.eskills2048.usecase.SetScoreUseCase;
import io.reactivex.Single;

public class MainGameViewModel {

  private final SetFinalScoreUseCase setFinalScoreUseCase;
  private final MainGameViewModelData data;
  private final SetScoreUseCase setScoreUseCase;
  private final GetRoomUseCase getRoomUseCase;
  private final SetGameStatusLocallyUseCase setGameStatusLocallyUseCase;
  private final GetGameStatusLocallyUseCase getGameStatusLocallyUseCase;

  public MainGameViewModel(SetScoreUseCase setScoreUseCase, SetFinalScoreUseCase setFinalScoreUseCase,
                           MainGameViewModelData data, GetRoomUseCase getRoomUseCase,
                           SetGameStatusLocallyUseCase setGameStatusLocallyUseCase,
                           GetGameStatusLocallyUseCase getGameStatusLocallyUseCase) {
    this.setScoreUseCase = setScoreUseCase;
    this.setFinalScoreUseCase = setFinalScoreUseCase;
    this.data = data;
    this.getRoomUseCase = getRoomUseCase;
    this.setGameStatusLocallyUseCase = setGameStatusLocallyUseCase;
    this.getGameStatusLocallyUseCase = getGameStatusLocallyUseCase;
  }

  public Single<RoomResponse> setFinalScore(long score) {
    return setFinalScoreUseCase.setFinalScore(data.getSession(), score);
  }

  public Single<RoomResponse> setScore(long score) {
    return setScoreUseCase.setScore(data.getSession(), score);
  }

  public String getWalletAddress() {
    return data.getWalletAddress();
  }

  public String getSession() {
    return data.getSession();
  }

  public Single<RoomResponse> getRoom() {
      return getRoomUseCase.getRoom(data.getSession());
  }

  public void setGameStatus(Tile[][] field, long score) {
    setGameStatusLocallyUseCase.setGameStatus(new LocalGameStatus(data.getSession(), field, score));
  }

  public LocalGameStatus getGameStatus() {
    return getGameStatusLocallyUseCase.getGameStatus();
  }
}
