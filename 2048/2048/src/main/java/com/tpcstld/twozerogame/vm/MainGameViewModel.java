package com.tpcstld.twozerogame.vm;

import com.tpcstld.twozerogame.model.RoomResponse;
import com.tpcstld.twozerogame.usecase.SetScoreUseCase;
import io.reactivex.Single;

public class MainGameViewModel {

  private final SetScoreUseCase setScoreUseCase;
  private final MainGameViewModelData data;

  public MainGameViewModel(SetScoreUseCase setScoreUseCase,
      MainGameViewModelData data) {
    this.setScoreUseCase = setScoreUseCase;
    this.data = data;
  }

  public Single<RoomResponse> setScore(int score) {
    return setScoreUseCase.setScore(data.getRoomId(), data.getUserId(), data.getWalletAddress(), data.getJwt(), score);
  }
}
