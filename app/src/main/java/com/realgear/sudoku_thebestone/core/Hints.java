package com.realgear.sudoku_thebestone.core;

import java.util.List;

public class Hints {
    private final List<Integer> hints;

    public Hints(List<Integer> hints) {
        this.hints = hints;
    }

    public List<Integer> getHints() {
        return hints;
    }
}
