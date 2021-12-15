package com.d4viddf.snake;

import android.app.Activity;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Display;
import android.view.View;

import androidx.annotation.Nullable;

public class MainActivity extends Activity {
    SnakeEngine snakeEngine;
    MediaPlayer mp;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mp = MediaPlayer.create(this,R.raw.background_sound);
        mp.start();
        mp.setLooping(true);
        View view = new View(this);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        snakeEngine = new SnakeEngine(this, size);
        setContentView(snakeEngine);

    }

    @Override
    protected void onResume() {
        super.onResume();
        snakeEngine.resume();
        mp.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        snakeEngine.pause();
        mp.pause();
    }
}