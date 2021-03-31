package com.tpcstld.twozerogame.vm;

import com.tpcstld.twozerogame.model.RoomResponse;
import com.tpcstld.twozerogame.usecase.SetFinalScoreUseCase;
import com.tpcstld.twozerogame.usecase.SetScoreUseCase;
import io.reactivex.Single;

public class MainGameViewModel {

  private final SetFinalScoreUseCase setFinalScoreUseCase;
  private final MainGameViewModelData data;
  private final SetScoreUseCase setScoreUseCase;

  public MainGameViewModel(SetScoreUseCase setScoreUseCase, SetFinalScoreUseCase setFinalScoreUseCase,
      MainGameViewModelData data) {
    this.setScoreUseCase = setScoreUseCase;
    this.setFinalScoreUseCase = setFinalScoreUseCase;
    this.data = data;
  }

  public Single<RoomResponse> setFinalScore(long score) {
    return setFinalScoreUseCase.setFinalScore(data.getRoomId(), data.getWalletAddress(), score);
  }

  public Single<RoomResponse> setScore(long score) {
    return setScoreUseCase.setScore(data.getRoomId(), data.getWalletAddress(), score);
  }
}
