package com.d4viddf.snake;

import android.graphics.Color;

import java.util.ArrayList;

public class Snake {

    public int Orienteton = 1;
    public int size = 0;
    private Position position;
    public int color = Color.argb(255, 255, 255, 255);
    ArrayList<BodyPart> cuerpo;

    public Snake() {
        cuerpo = new ArrayList<>();
    }

    public Snake(Snake snake) {
        this.position = snake.getPosition();
        this.color = snake.getColor();
        cuerpo = snake.cuerpo;
    }

    public Snake(Position position) {
        this.position = position;
        cuerpo = new ArrayList<>();
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

    public void addBodyPart(Object o) {
        cuerpo.add(new BodyPart(o));
    }

}