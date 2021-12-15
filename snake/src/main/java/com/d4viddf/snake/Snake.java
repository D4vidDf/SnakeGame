package com.d4viddf.snake;

import android.graphics.Color;

public class Snake {
    public int Orienteton =1;
    public int size = 0;
    private Position position;
    public int color = Color.argb(255,255,255,255);

    public Snake() {
    }

    public Snake(Position position) {
        this.position = position;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public int getOrienteton() {
        return Orienteton;
    }

    public void setOrienteton(int orienteton) {
        Orienteton = orienteton;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}