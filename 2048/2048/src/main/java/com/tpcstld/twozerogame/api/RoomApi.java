package com.tpcstld.twozerogame.api;

import com.tpcstld.twozerogame.model.RoomRequest;
import com.tpcstld.twozerogame.model.RoomResponse;
import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.PATCH;
import retrofit2.http.Path;

public interface RoomApi {

  @PATCH("room/{roomId}") Single<RoomResponse> patchRoom(@Path("roomId") String roomId, @Body RoomRequest roomRequest);
}
