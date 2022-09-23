package com.realgear.sudoku_thebestone.core;

import java.util.ArrayList;
import java.util.List;

public class GameMode {

    public GameType mGameType;

    public int mCurrentLevel;

    public GameMode(GameType gameType)
    {
        mGameType = gameType;
        mCurrentLevel = 1;
    }

    public int getCurrentLevel()
    {
        return mCurrentLevel;
    }

    public List<Integer> getGridSize() {
        List<Integer> grids = new ArrayList<>();
        switch (mGameType) {
            case Insane:
                grids.add(4);
                break;
            case Extreme:
                grids.add(3);
                grids.add(4);
                break;
            case Hard:
                grids.add(3);
                break;
            case Medium:
                grids.add(3);
                grids.add(2);
            case Easy:
                grids.add(2);
            case Beginner:
                grids.add(2);
        }
        return grids;
    }

    public String getGameMode() {
        return mGameType.name();
    }

    public void setLevel(int level) {
        mCurrentLevel = level;
    }
}
