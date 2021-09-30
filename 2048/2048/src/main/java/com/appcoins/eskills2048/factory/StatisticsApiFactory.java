package com.appcoins.eskills2048.factory;

import com.appcoins.eskills2048.BuildConfig;
import com.appcoins.eskills2048.api.GeneralPlayerStats;
import com.appcoins.eskills2048.util.LogInterceptor;
import com.google.gson.Gson;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class StatisticsApiFactory {
  public static final String ENDPOINT = BuildConfig.BASE_HOST_SKILLS;

  public static GeneralPlayerStats buildRoomApi() {
    return new Retrofit.Builder().baseUrl(ENDPOINT)
        .client(buildOkHttp())
        .addConverterFactory(GsonConverterFactory.create(buildGson()))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
        .create(GeneralPlayerStats.class);
  }

  private static Gson buildGson() {
    return new Gson();
  }

  private static OkHttpClient buildOkHttp() {
    OkHttpClient.Builder builder = new OkHttpClient.Builder();
    if (BuildConfig.DEBUG) {
      builder = builder.addInterceptor(new LogInterceptor());
    }
    return builder.build();
  }
}
