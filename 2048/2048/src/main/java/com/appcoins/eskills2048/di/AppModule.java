package com.appcoins.eskills2048.di;

import com.appcoins.eskills2048.BuildConfig;
import com.appcoins.eskills2048.api.GeneralPlayerStats;
import com.appcoins.eskills2048.api.RoomApi;
import com.appcoins.eskills2048.model.UserDetailsHelper;
import com.appcoins.eskills2048.util.GameFieldConverter;
import com.appcoins.eskills2048.util.LogInterceptor;
import com.google.gson.Gson;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import javax.inject.Singleton;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module @InstallIn(SingletonComponent.class) public final class AppModule {
  private static final String BASE_HOST_SKILLS = BuildConfig.BASE_HOST_SKILLS;

  @Singleton @Provides public Gson provideGson() {
    return new Gson();
  }

  @Singleton @Provides public OkHttpClient provideOkHttp() {
    OkHttpClient.Builder builder = new OkHttpClient.Builder();
    if (BuildConfig.DEBUG) {
      builder = builder.addInterceptor(new LogInterceptor());
    }
    return builder.build();
  }

  @Singleton @Provides public RoomApi provideRoomApi(OkHttpClient okHttpClient, Gson gson) {
    return new Retrofit.Builder().baseUrl(BASE_HOST_SKILLS)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
        .create(RoomApi.class);
  }

  @Singleton @Provides
  public GeneralPlayerStats provideGeneralPlayerStats(OkHttpClient okHttpClient, Gson gson) {
    return new Retrofit.Builder().baseUrl(BASE_HOST_SKILLS)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
        .create(GeneralPlayerStats.class);
  }

  @Singleton @Provides public GameFieldConverter provideGameFieldConverter(Gson gson) {
    return new GameFieldConverter(gson);
  }

  @Singleton @Provides public UserDetailsHelper provideUserDetailsHelper() {
    return new UserDetailsHelper();
  }
}
