package com.appcoins.eskills2048.di;

import android.content.Context;
import com.appcoins.eskills2048.api.GeneralPlayerStats;
import com.appcoins.eskills2048.api.RoomApi;
import com.appcoins.eskills2048.repository.LocalGameStatusRepository;
import com.appcoins.eskills2048.repository.RoomRepository;
import com.appcoins.eskills2048.repository.StatisticsRepository;
import com.appcoins.eskills2048.util.GameFieldConverter;
import com.appcoins.eskills2048.util.UserDataStorage;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;
import javax.inject.Singleton;

@Module @InstallIn(SingletonComponent.class) public final class RepositoryModule {
  private static final String SHARED_PREFERENCES_NAME = "SKILL_SHARED_PREFERENCES";

  @Singleton @Provides public RoomRepository provideRoomRepository(RoomApi roomApi) {
    return new RoomRepository(roomApi);
  }

  @Singleton @Provides
  public LocalGameStatusRepository provideLocalGameStatusRepository(UserDataStorage userDataStorage,
      GameFieldConverter gameFieldConverter) {
    return new LocalGameStatusRepository(userDataStorage, gameFieldConverter);
  }

  @Singleton @Provides
  public UserDataStorage provideUserDataStorage(@ApplicationContext Context context) {
    return new UserDataStorage(context, SHARED_PREFERENCES_NAME);
  }

  @Singleton @Provides
  public StatisticsRepository provideStatisticsRepository(GeneralPlayerStats api) {
    return new StatisticsRepository(api);
  }
}
