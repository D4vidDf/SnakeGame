package com.d4viddf.snake;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;

import androidx.annotation.Nullable;


public class MainActivity extends Activity {

    SnakeEngine snakeEngine;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
    }

    @Override
    protected void onPause() {
        super.onPause();
        snakeEngine.pause();
    }
}