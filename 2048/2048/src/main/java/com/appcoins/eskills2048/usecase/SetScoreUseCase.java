package com.appcoins.eskills2048.usecase;

import com.appcoins.eskills2048.model.RoomResponse;
import com.appcoins.eskills2048.model.UserStatus;
import com.appcoins.eskills2048.repository.RoomRepository;
import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import retrofit2.HttpException;

public class SetScoreUseCase {

  private final RoomRepository roomRepository;

  public SetScoreUseCase(RoomRepository roomRepository) {
    this.roomRepository = roomRepository;
  }

  public Single<RoomResponse> setScore(String session, long score) {
    return roomRepository.patch(session, score, UserStatus.PLAYING)
        .subscribeOn(Schedulers.io())
        .retryWhen(throwableFlowable -> {
          AtomicInteger counter = new AtomicInteger();
          return throwableFlowable.takeWhile(throwable -> counter.getAndIncrement() != 3
              && (throwable instanceof SocketTimeoutException
              || throwable instanceof HttpException))
              .flatMap(throwable -> Flowable.timer(counter.get(), TimeUnit.SECONDS));
        });
  }
}
