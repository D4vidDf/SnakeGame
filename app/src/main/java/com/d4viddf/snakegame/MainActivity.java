package com.d4viddf.snakegame;

import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.GamesClient;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    SnakeEngine snakeEngine;
    MediaPlayer mp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mp = MediaPlayer.create(this, R.raw.retrobit);
        mp.setLooping(true);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        snakeEngine = new SnakeEngine(this, size, mp);
        setContentView(snakeEngine);
        if (GoogleSignIn.getLastSignedInAccount(this) != null) {
            GamesClient gamesClient = Games.getGamesClient(this, Objects.requireNonNull(GoogleSignIn.getLastSignedInAccount(this)));
            gamesClient.setViewForPopups(findViewById(android.R.id.content));
            gamesClient.setGravityForPopups(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        snakeEngine.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        snakeEngine.pause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}