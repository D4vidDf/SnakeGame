package com.d4viddf.snakegame;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Display;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    SnakeEngine snakeEngine;
    MediaPlayer mp;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mp = MediaPlayer.create(this,R.raw.retrobit);
        mp.setLooping(true);
        View view = new View(this);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        snakeEngine = new SnakeEngine(this, size,mp);

        setContentView(snakeEngine);


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