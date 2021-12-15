package com.d4viddf.snakegame;

public class Position {
    public int PosX, PosY;

    public Position() {
    }

    public Position(int posX, int posY) {
        PosX = posX;
        PosY = posY;
    }

    public int getPosX() {
        return PosX;
    }

    public void setPosX(int posX) {
        PosX = posX;
    }

    public int getPosY() {
        return PosY;
    }

    public void setPosY(int posY) {
        PosY = posY;
    }
}
