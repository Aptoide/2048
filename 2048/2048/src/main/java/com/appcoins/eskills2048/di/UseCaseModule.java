package com.appcoins.eskills2048.di;

import com.appcoins.eskills2048.repository.LocalGameStatusRepository;
import com.appcoins.eskills2048.repository.RoomRepository;
import com.appcoins.eskills2048.repository.StatisticsRepository;
import com.appcoins.eskills2048.usecase.GetGameStatusLocallyUseCase;
import com.appcoins.eskills2048.usecase.GetRoomUseCase;
import com.appcoins.eskills2048.usecase.GetUserStatisticsUseCase;
import com.appcoins.eskills2048.usecase.SetFinalScoreUseCase;
import com.appcoins.eskills2048.usecase.SetGameStatusLocallyUseCase;
import com.appcoins.eskills2048.usecase.SetScoreUseCase;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import javax.inject.Singleton;

@Module @InstallIn(SingletonComponent.class) public final class UseCaseModule {

  @Singleton @Provides public GetGameStatusLocallyUseCase provideGetGameStatusLocallyUseCase(
      LocalGameStatusRepository localGameStatusRepository, GetRoomUseCase getRoomUseCase) {
    return new GetGameStatusLocallyUseCase(localGameStatusRepository, getRoomUseCase);
  }

  @Singleton @Provides public GetRoomUseCase provideGetRoomUseCase(RoomRepository roomRepository) {
    return new GetRoomUseCase(roomRepository);
  }

  @Singleton @Provides public GetUserStatisticsUseCase provideGetUserStatisticsUseCase(
      StatisticsRepository statisticsRepository) {
    return new GetUserStatisticsUseCase(statisticsRepository);
  }

  @Singleton @Provides
  public SetFinalScoreUseCase provideSetFinalScoreUseCase(RoomRepository roomRepository) {
    return new SetFinalScoreUseCase(roomRepository);
  }

  @Singleton @Provides public SetGameStatusLocallyUseCase provideSetGameStatusLocallyUseCase(
      LocalGameStatusRepository localGameStatusRepository) {
    return new SetGameStatusLocallyUseCase(localGameStatusRepository);
  }

  @Singleton @Provides
  public SetScoreUseCase provideSetScoreUseCase(RoomRepository roomRepository) {
    return new SetScoreUseCase(roomRepository);
  }
}
