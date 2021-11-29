package com.appcoins.eskills2048.model;

import androidx.annotation.NonNull;
import com.google.gson.Gson;
import io.reactivex.Single;
import javax.inject.Inject;
import retrofit2.HttpException;



public class RoomApiMapper {
  private class Response {
    Detail detail;
  }

  private static class Detail {
    String code;
  }
  @Inject Gson gson;

  public Single<RoomResponse> map(Single<RoomResponse> roomResponse){
    return roomResponse
        .flatMap(response -> {
          response.setStatusCode(RoomResponse.StatusCode.SUCCESSFUL_RESPONSE);
          return Single.just(response);
        })
        .onErrorReturn(this::mapException);
  }


  private RoomResponse mapException(@NonNull Throwable throwable){

    RoomResponse.StatusCode status = RoomResponse.StatusCode.GENERIC_ERROR;
    if(throwable instanceof HttpException) {
      HttpException exception = (HttpException) throwable;
      retrofit2.Response<?> errorResponse = exception.response();

      try {
        if (errorResponse !=null && errorResponse.errorBody()!=null) {
          Response gsonResponse = this.gson.fromJson(
              errorResponse.errorBody().charStream(),Response.class
          );
          status = RoomResponse.StatusCode.valueOf(gsonResponse.detail.code);
        }
      }
      catch (Exception e){
        e.printStackTrace();
      }
    }
    RoomResponse roomResponse = new RoomResponse();
    roomResponse.setStatusCode(status);
    return roomResponse;
  }

}

