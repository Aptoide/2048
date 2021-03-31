package com.tpcstld.twozerogame.vm;

import com.tpcstld.twozerogame.model.RoomResponse;
import com.tpcstld.twozerogame.usecase.SetFinalScoreUseCase;
import io.reactivex.Single;

public class MainGameViewModel {

  private final SetFinalScoreUseCase setFinalScoreUseCase;
  private final MainGameViewModelData data;

  public MainGameViewModel(SetFinalScoreUseCase setFinalScoreUseCase,
      MainGameViewModelData data) {
    this.setFinalScoreUseCase = setFinalScoreUseCase;
    this.data = data;
  }

  public Single<RoomResponse> setFinalScore(long score) {
    return setFinalScoreUseCase.setFinalScore(data.getRoomId(), data.getWalletAddress(), score);
  }
}
