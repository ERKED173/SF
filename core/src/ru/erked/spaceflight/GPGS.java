package ru.erked.spaceflight;

import java.util.List;


/**
 * Created by Andrey (cb) Mikheev
 * GPGS
 * 26.09.2016
 */
public interface GPGS {
    public void connect();
    public void disconnect();
    public boolean isConnected();
    public void signOut();

    public void unlockAchievement(int n);
    public void unlockIncrementAchievement(int n, int count);
    public void showAchievements();

    public void submitScore(long score, int n);
    public void showLeaderboard(int n);
}
