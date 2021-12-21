package com.appcoins.eskills2048.model;

import androidx.annotation.NonNull;
import com.google.gson.Gson;
import io.reactivex.Single;
import retrofit2.HttpException;

public class RoomApiMapper {
  private final Gson gson;

  public RoomApiMapper(Gson gson) {
    this.gson = gson;
  }

  private static class Response {
    Detail detail;
  }

  private static class Detail {
    String code;
  }

  public Single<RoomResponse> map(Single<RoomResponse> roomResponse) {
    return roomResponse.flatMap(response -> {
          response.setStatusCode(RoomResponse.StatusCode.SUCCESSFUL_RESPONSE);
          return Single.just(response);
        })
        .onErrorResumeNext(this::mapException);
  }

  private Single<RoomResponse> mapException(@NonNull Throwable throwable) {

    RoomResponse.StatusCode status = RoomResponse.StatusCode.GENERIC_ERROR;
    if (throwable instanceof HttpException) {
      HttpException exception = (HttpException) throwable;
      if (exception.code() == 403) {
        retrofit2.Response<?> errorResponse = exception.response();
        try {
          if (errorResponse != null && errorResponse.errorBody() != null) {
            Response gsonResponse = this.gson.fromJson(errorResponse.errorBody()
                .charStream(), Response.class);
            status = RoomResponse.StatusCode.valueOf(gsonResponse.detail.code);
          }
          RoomResponse roomResponse = new RoomResponse();
          roomResponse.setStatusCode(status);
          return Single.just(roomResponse);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }
    return Single.error(throwable);
  }
}

