package com.appcoins.eskills2048.vm;

import java.io.Serializable;

public class MainGameViewModelData {

    private final String userId;
    private final String walletAddress;
    private final String session;
    private final Serializable matchEnvironment;

    public MainGameViewModelData(String userId, String walletAddress, String session, Serializable matchEnvironment) {
        this.userId = userId;
        this.walletAddress = walletAddress;
        this.session = session;
        this.matchEnvironment = matchEnvironment;
    }

    public String getWalletAddress() {
        return walletAddress;
    }

    public String getSession() {
        return session;
    }

    public Serializable getMatchEnvironment() {
        return matchEnvironment;
    }
}
