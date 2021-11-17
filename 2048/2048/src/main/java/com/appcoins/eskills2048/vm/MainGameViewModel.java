package com.appcoins.eskills2048.vm;

import com.appcoins.eskills2048.model.RoomResponse;
import com.appcoins.eskills2048.usecase.GetRoomUseCase;
import com.appcoins.eskills2048.usecase.SetFinalScoreUseCase;
import com.appcoins.eskills2048.usecase.SetScoreUseCase;

import java.io.Serializable;

import io.reactivex.Single;

public class MainGameViewModel {

  private final SetFinalScoreUseCase setFinalScoreUseCase;
  private final MainGameViewModelData data;
  private final SetScoreUseCase setScoreUseCase;
  private final GetRoomUseCase getRoomUseCase;

  public MainGameViewModel(SetScoreUseCase setScoreUseCase, SetFinalScoreUseCase setFinalScoreUseCase,
                           MainGameViewModelData data, GetRoomUseCase getRoomUseCase) {
    this.setScoreUseCase = setScoreUseCase;
    this.setFinalScoreUseCase = setFinalScoreUseCase;
    this.data = data;
    this.getRoomUseCase = getRoomUseCase;
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

  public Serializable getMatchEnvironment(){return data.getMatchEnvironment();}
}
