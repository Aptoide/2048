package com.tpcstld.twozerogame.api;

import com.tpcstld.twozerogame.model.PatchRoomRequest;
import com.tpcstld.twozerogame.model.RoomResponse;
import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;

public interface RoomApi {

  @PATCH("room/") Single<RoomResponse> patchRoom(@Header("authorization") String authorization,
      @Body PatchRoomRequest patchRoomRequest);

  @GET("room/") Single<RoomResponse> getRoom(@Header("authorization") String authorization);
}
