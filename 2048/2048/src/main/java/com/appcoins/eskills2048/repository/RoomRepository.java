package com.appcoins.eskills2048.repository;

import com.appcoins.eskills2048.api.RoomApi;
import com.appcoins.eskills2048.model.PatchRoomRequest;
import com.appcoins.eskills2048.model.RoomResponse;
import com.appcoins.eskills2048.model.UserStatus;
import io.reactivex.Flowable;
import io.reactivex.Single;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import retrofit2.HttpException;

public class RoomRepository {

  public static final String BEARER_ = "Bearer ";

  private final RoomApi roomApi;

  public RoomRepository(RoomApi roomApi) {
    this.roomApi = roomApi;
  }

  public Single<RoomResponse> patch(String session, long score, UserStatus status) {
    PatchRoomRequest patchRoomRequest = new PatchRoomRequest();
    patchRoomRequest.setScore(score);
    patchRoomRequest.setStatus(status);
    return roomApi.patchRoom(BEARER_ + session, patchRoomRequest)
        .retryWhen(throwableFlowable -> {
          AtomicInteger counter = new AtomicInteger();
          return throwableFlowable.takeWhile(throwable -> counter.getAndIncrement() != 3
              && (throwable instanceof SocketTimeoutException
              || throwable instanceof HttpException))
              .flatMap(throwable -> Flowable.timer(counter.get(), TimeUnit.SECONDS));
        });
  }

  public Single<RoomResponse> getRoom(String session) {
    return roomApi.getRoom(BEARER_ + session);
  }
}
