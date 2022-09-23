package com.realgear.sudoku_thebestone.core;

public class Undo {
    private final int x;
    private final int y;
    private final int value;

    public Undo(int x, int y, int value) {
        this.x = x;
        this.y = y;
        this.value = value;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getValue() {
        return value;
    }
}
