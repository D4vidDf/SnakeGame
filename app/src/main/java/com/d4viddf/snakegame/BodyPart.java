package com.d4viddf.snakegame;

import android.graphics.Color;

public class BodyPart {
    Position position;
    int color = Color.argb(160,196,50,255);

    public BodyPart(Object bp) {
        clone(bp);
    }



    public void setPosition(Position position) {
        this.position = new Position(position);
    }

    public void setColor(int color) {
        this.color = color;
    }

    public Position getPosition() {
        return position;
    }

    public int getColor() {
        return color;
    }

    public void clone(Object ob) {
        try {
            setColor(((BodyPart) ob).color);
            setPosition(((BodyPart) ob).position);
        } catch (Exception ignored) {

        }
        try {
            this.setColor(((Snake) ob).getColor());
            setPosition(((Snake) ob).getPosition());
        } catch (Exception ignored) {

        }
    }
}
