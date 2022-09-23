package com.realgear.sudoku_thebestone.core;

public class CellState {

    public enum State {
        BLANK,
        FILLED,
        FIXED
    };

    public enum Status {
        NORMAL,
        HIGHLIGHTED,
        ERROR,
        SOLVED
    };
}
