package com.realgear.sudoku_thebestone.utils;

import java.util.Random;

public enum Level {
    EASY(0.25f,0.35f, Iteration.RANDOM),
    MEDIUM(0.36f, 0.46f, Iteration.RANDOM),
    HARD(0.47f, 0.57f, Iteration.RANDOM),
    EXTREME(0.58f, 0.69f, Iteration.RANDOM),
    INSANE(0.70f, 0.90f, Iteration.RANDOM);

    private final float min;
    private final float max;
    private final Iteration iterationType;

    Level(float min, float max, Iteration iterationType)
    {
        this.min = min;
        this.max = max;

        this.iterationType = iterationType;
    }

    public int getBlankCellsNumber(Random mRandom, int cells) {
        int s_min = (int)(cells * min);
        int s_max = (int)(cells * max);
        return mRandom.nextInt((s_max - s_min) + 1) + s_min;
    }

    public Iteration getIterationType()
    {
        return iterationType;
    }
}
