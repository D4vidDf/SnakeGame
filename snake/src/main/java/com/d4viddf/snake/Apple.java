package com.d4viddf.snake;

import android.graphics.Color;

import java.util.Random;

public class Apple {

    private Position position;
    boolean pos = false;
    public int color = Color.argb(255, 255, 0, 0);

    public Apple() {
        position = new Position();
    }

    public Apple(Position position) {
        this.position = position;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public void setPosition(int x, int y) {
        if (!pos) {
            Random random = new Random();
            int posx ;
            int posy ;
            posx = random.nextInt(x) + 5;
            posy = random.nextInt(y) + 5;
            position = new Position(posx, posy);
            pos= true;
        }

    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

}
