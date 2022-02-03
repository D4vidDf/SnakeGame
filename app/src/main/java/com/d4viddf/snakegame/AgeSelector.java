package com.d4viddf.snakegame;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class AgeSelector extends AppCompatActivity {
    TextInputEditText age;
    ImageView plus, minus;
    MaterialButton aceptar;
    boolean pressed;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_age_selector);

        age = findViewById(R.id.age);
        plus = findViewById(R.id.plus);
        minus = findViewById(R.id.minus);
        aceptar = findViewById(R.id.aceptar);


        plus.setOnClickListener(v -> plus());
        minus.setOnClickListener(v -> minus());

        aceptar.setOnClickListener(v -> {
            new Utils().vibrateButtonApple(getApplicationContext());
            regist();
        });

        if (new PrefManager(this).isAge()) {
            Intent intent = new Intent(AgeSelector.this, StartActivity.class);
            startActivity(intent);
        }

    }

    private void regist() {
        new PrefManager(this).setAge(Integer.parseInt(age.getText().toString()));
        Intent intent = new Intent(AgeSelector.this, StartActivity.class);
        startActivity(intent);
    }

    private void minus() {
        if (Integer.parseInt(age.getText().toString()) > 0) {
            int num = Integer.parseInt(age.getText().toString()) - 1;
            age.setText(String.valueOf(num));
        } else {
            age.setText("99");
        }
        new Utils().vibrateButtonApple(getApplicationContext());
    }

    private void plus() {
        if (Integer.parseInt(age.getText().toString()) < 99) {
            int num = Integer.parseInt(age.getText().toString()) + 1;
            age.setText(String.valueOf(num));
        } else {
            age.setText("0");
        }
        new Utils().vibrateButtonApple(getApplicationContext());
    }
}