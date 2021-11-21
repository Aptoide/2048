package com.appcoins.eskills2048.model;

import androidx.annotation.NonNull;
import java.util.Objects;
import org.json.JSONObject;
import retrofit2.HttpException;

public class RoomApiMapper {

  public RoomResponseErrorCode mapException(@NonNull Throwable throwable){
    String body;
    RoomResponseErrorCode response;
    if(throwable instanceof HttpException) {
      HttpException exception = (HttpException) throwable;
      try {
        body = Objects.requireNonNull(Objects.requireNonNull(exception.response())
            .errorBody())
            .string();
        JSONObject detail = new JSONObject(body).getJSONObject("detail");
        response = RoomResponseErrorCode.valueOf(detail.getString("code"));
      }
      catch (Exception e){
        e.printStackTrace();
        response = RoomResponseErrorCode.DEFAULT_ERROR;
      }
    }
    else{
      response = RoomResponseErrorCode.DEFAULT_ERROR;
    }
    return response;
  }
}

