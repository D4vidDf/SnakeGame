package com.d4viddf.snakegame;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class Controls extends AppCompatActivity {
    MaterialButton play;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controls);
        play = findViewById(R.id.play);
        play.setOnClickListener(v -> {
            Intent intent = new Intent(Controls.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }
}