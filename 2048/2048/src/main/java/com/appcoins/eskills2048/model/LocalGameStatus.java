package com.appcoins.eskills2048.model;

import com.appcoins.eskills2048.Tile;

import java.util.Arrays;

public class LocalGameStatus {
    private String session;
    private Tile[][] field;
    private long score;

    public LocalGameStatus(String session, Tile[][] field, long score) {
        this.session = session;
        this.field = field;
        this.score = score;
    }

    public String getSession() {
        return session;
    }

    public Tile[][] getField() {
        return field;
    }

    public long getScore() {
        return score;
    }

    @Override
    public String toString() {
        return "LocalGameStatus{" +
                "session='" + session + '\'' +
                ", field=" + Arrays.toString(field) +
                ", score=" + score +
                '}';
    }
}
