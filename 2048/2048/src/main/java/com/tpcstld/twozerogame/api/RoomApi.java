package com.tpcstld.twozerogame.api;

import com.tpcstld.twozerogame.model.PatchRoomRequest;
import com.tpcstld.twozerogame.model.RoomResponse;
import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.PATCH;
import retrofit2.http.Path;

public interface RoomApi {

  @PATCH("room/{room_id}/{wallet_address}") Single<RoomResponse> patchRoom(
      @Path("room_id") String roomId, @Path("wallet_address") String walletAddress,
      @Body PatchRoomRequest patchRoomRequest);
}
