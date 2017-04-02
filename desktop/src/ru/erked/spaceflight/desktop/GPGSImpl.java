package ru.erked.spaceflight.desktop;

import com.badlogic.gdx.Gdx;

import ru.erked.spaceflight.GPGS;


/**
 * Created by Andrey (cb) Mikheev
 * TutorialGPGS
 * 26.09.2016
 */
public class GPGSImpl implements GPGS {

    private final String[] ACHEIVEMENT = {
            "1 ачивка",
            "2 ачивка",
            "3 ачивка",
            "4 ачивка",
            "5 ачивка",
    };

    private final String className = ">>> " + GPGS.class.getSimpleName();

    @Override
    public void connect() {
        connectingState = true;
        Gdx.app.debug( className, "connect" );
    }

    @Override
    public void disconnect() {
        connectingState = false;
        Gdx.app.debug( className, "disconnect" );
    }

    private boolean connectingState = false;

    @Override
    public boolean isConnected() {
        return connectingState;
    }

    @Override
    public void signOut() {
        Gdx.app.debug( className, "sign out" );
    }

    @Override
    public void unlockAchievement( int n ) {
        String about = "";
        if ( n >= ACHEIVEMENT.length ) { about = "Error in acheivement!!! <<<"; }
        else { about = ACHEIVEMENT[ n ]; }

        Gdx.app.debug( className, "unlock " + n + " achievement" );
        Gdx.app.debug( className, about );
    }

    @Override
    public void unlockIncrementAchievement( int n, int count ) {
        String about = "";
        if ( n >= ACHEIVEMENT.length ) { about = "Error in acheivementя!!! <<<<<"; }
        else { about = ACHEIVEMENT[ n ]; }

        String s = "try to unlock increment achievement (" + n + ") by " + count + " value";
        Gdx.app.debug( className, s );
        Gdx.app.debug( className, about );
    }

    @Override
    public void showAchievements() {
        Gdx.app.debug( className, "show achievements" );
    }

    @Override
    public void submitScore( long score, int n ) {
        Gdx.app.debug( className, "submit " + score + " scores" );
    }

    @Override
    public void showLeaderboard(int n) {
        Gdx.app.debug( className, "show leaderboard" );
    }
}
