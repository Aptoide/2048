package com.appcoins.eskills2048.vm;

import com.appcoins.eskills2048.model.RoomResponse;
import com.appcoins.eskills2048.model.User;
import com.appcoins.eskills2048.usecase.GetRoomUseCase;
import com.appcoins.eskills2048.usecase.SetFinalScoreUseCase;
import com.appcoins.eskills2048.usecase.SetScoreUseCase;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

import io.reactivex.Single;

public class MainGameViewModel {

    private final SetFinalScoreUseCase setFinalScoreUseCase;
    private final MainGameViewModelData data;
    private final SetScoreUseCase setScoreUseCase;
    private final GetRoomUseCase getRoomUseCase;

    public MainGameViewModel(SetScoreUseCase setScoreUseCase, SetFinalScoreUseCase setFinalScoreUseCase,
                             MainGameViewModelData data, GetRoomUseCase getRoomUseCase) {
        this.setScoreUseCase = setScoreUseCase;
        this.setFinalScoreUseCase = setFinalScoreUseCase;
        this.data = data;
        this.getRoomUseCase = getRoomUseCase;
    }

    public Single<String> getLeaderBoard() {
        return getRoomUseCase.getRoom(data.getSession())
                .toObservable()
                .map(this::getLeaderBoardFromUserList)
                .singleOrError();
    }

    private String getLeaderBoardFromUserList(RoomResponse roomResponse) {
        StringBuilder leaderBoard = new StringBuilder();
        List<User> roomUsers = roomResponse.getUsers();
        Collections.sort(roomUsers, (user1, user2) -> (user1.getScore() > user2.getScore()) ? -1 : 0);

        for (int i = 0; i < roomUsers.size(); i++) {
            User user = roomUsers.get(i);
            leaderBoard.append(String.format(
                    Locale.US, "%d - %s (%d points)", i + 1, user.getUserName(), user.getScore()));
            if (i < roomUsers.size() - 1) {
                leaderBoard.append("\n");
            }
        }
        return leaderBoard.toString();
    }


    public Single<RoomResponse> setFinalScore(long score) {
        return setFinalScoreUseCase.setFinalScore(data.getSession(), score);
    }

    public Single<RoomResponse> setScore(long score) {
        return setScoreUseCase.setScore(data.getSession(), score);
    }

    public String getWalletAddress() {
        return data.getWalletAddress();
    }

    public String getSession() {
        return data.getSession();
    }
}
