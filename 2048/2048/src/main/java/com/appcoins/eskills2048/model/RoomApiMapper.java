package com.appcoins.eskills2048.model;

import androidx.annotation.NonNull;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import java.util.Objects;
import org.json.JSONObject;
import retrofit2.HttpException;



public class RoomApiMapper {
  private class Response{
    Detail detail;
  }
  private class Detail{
    String code;
    String message;
  }

  public RoomResponseErrorCode mapException(@NonNull Throwable throwable){
    RoomResponseErrorCode response;
    if(throwable instanceof HttpException) {
      HttpException exception = (HttpException) throwable;
      try {
        Gson gson = new Gson();
        Response gsonResponse = gson.fromJson(Objects.requireNonNull(Objects.requireNonNull(exception.response())
            .errorBody())
            .charStream(),Response.class);
        response = RoomResponseErrorCode.valueOf(gsonResponse.detail.code);
      }
      catch (Exception e){
        e.printStackTrace();
        response = RoomResponseErrorCode.GENERIC_ERROR;
      }
    }
    else{
      response = RoomResponseErrorCode.GENERIC_ERROR;
    }
    return response;
  }

}

