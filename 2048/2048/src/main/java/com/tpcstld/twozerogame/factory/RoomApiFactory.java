package com.tpcstld.twozerogame.factory;

import com.google.gson.Gson;
import com.tpcstld.twozerogame.api.RoomApi;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
//import retrofit2.converter.gson.GsonConverterFactory;

public class RoomApiFactory {

  public static final String ENDPOINT =
      "https://9c512579-41fa-420a-bba9-67e9d231681e.mock.pstmn.io";

  public static final RoomApi buildRoomApi() {
    return new Retrofit.Builder().baseUrl(ENDPOINT)
        .client(buildOkHttp())
        .addConverterFactory(GsonConverterFactory.create(buildGson()))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
        .create(RoomApi.class);
  }

  private static Gson buildGson() {
    return new Gson();
  }

  private static OkHttpClient buildOkHttp() {
    return new OkHttpClient();
  }
}
