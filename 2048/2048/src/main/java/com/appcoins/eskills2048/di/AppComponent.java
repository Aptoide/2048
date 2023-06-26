package com.appcoins.eskills2048.di;

import com.appcoins.eskills2048.BuildConfig;
import com.appcoins.eskills2048.api.RoomApi;
import com.appcoins.eskills2048.model.RoomApiMapper;
import com.appcoins.eskills2048.util.LogInterceptor;
import com.google.gson.Gson;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import javax.inject.Named;
import javax.inject.Singleton;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module @InstallIn(SingletonComponent.class) public final class AppComponent {
  private static final String SHARED_PREFERENCES_NAME = "SKILL_SHARED_PREFERENCES";
  private static final String BASE_HOST_SKILLS = BuildConfig.BASE_HOST_SKILLS;

  @Singleton @Provides @Named("preferencesName") public String provideSharedPreferencesName() {
    return SHARED_PREFERENCES_NAME;
  }

  @Singleton @Provides public Gson provideGson() {
    return new Gson();
  }

  @Singleton @Provides public OkHttpClient provideOkHttp() {
    OkHttpClient.Builder builder = new OkHttpClient.Builder();
    if (BuildConfig.DEBUG) {
      builder.addInterceptor(new LogInterceptor());
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

  @Singleton @Provides public RoomApiMapper provideRoomApiMapper(Gson gson) {
    return new RoomApiMapper(gson);
  }
}
