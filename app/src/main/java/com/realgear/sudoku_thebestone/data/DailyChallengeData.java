package com.realgear.sudoku_thebestone.data;

import android.content.Context;
import android.util.Log;

import com.realgear.sudoku_thebestone.core.GameType;
import com.realgear.sudoku_thebestone.core.SudokuTypes;
import com.realgear.sudoku_thebestone.utils.SamuraiGridType;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DailyChallengeData extends DataPref {

    private final String KEY_COMPLETED      = "COMPLETED";
    private final String KEY_LEVEL          = "LEVEL";
    private final String KEY_GRID_SIZE      = "GRID_SIZE";
    private final String KEY_GAME_MODE      = "GAME_MODE";
    private final String KEY_GRID_TYPE      = "GRID_TYPE";
    private final String KEY_SUDOKU_TYPE    = "SUDOKU_TYPE";
    private final String KEY_CREATED_DATE   = "CREATED_DATE";

    private final String KEY_SUDOKU_CREATED = "SUDOKU_CREATED";

    private String  mCreatedDate;
    private int     mGridSize;
    private int     mGridType;
    private int     mGameMode;
    private int     mSudokuType;

    public DailyChallengeData(Context context) {
        super(context, DailyChallengeData.class.getSimpleName());
    }

    public boolean canResume() {
        String oldDate = getString(KEY_CREATED_DATE);

        if (oldDate.isEmpty()) {
            Log.e("DailyCData", "Old Date Is Empty");
            return false;
        }

        String curDate = LocalDateTime.now().format(DateTimeFormatter.BASIC_ISO_DATE).toString();

        if(!isSudokuCreated()) {
            Log.e("DailyCData", "Sudoku !Created");
            return false;
        }

        if(!oldDate.equals(curDate)) {
            setSudokuCreated(false);
            setIsCompleted(false);
            Log.e("DailyCData", "Date Doesn't Match");
        }

        return (oldDate.equals(curDate));
    }

    public void setCreatedDate() {
        String curDate = LocalDateTime.now().format(DateTimeFormatter.BASIC_ISO_DATE);
        putString(KEY_CREATED_DATE, curDate);
    }

    public boolean isSudokuCreated() {
        return getBoolean(KEY_SUDOKU_CREATED);
    }
    public void setSudokuCreated(boolean value) {
        putBoolean(KEY_SUDOKU_CREATED, value);
    }

    public void setSudoku() {
        int level = getLevel();

        if(level < 10) {
            mGameMode = GameType.Beginner.ordinal();
        }
        else if(level > 10 && level < 20) {
            mGameMode = GameType.Easy.ordinal();
        }
        else if(level > 20 && level < 30) {
            mGameMode = GameType.Medium.ordinal();
        }
        else if(level > 30 && level < 75) {
            mGameMode = GameType.Hard.ordinal();
        }
        else if(level > 75 && level < 100) {
            mGameMode = GameType.Extreme.ordinal();
        }
        else if(level > 100) {
            mGameMode = GameType.Insane.ordinal();
        }

        mCreatedDate = LocalDateTime.now().format(DateTimeFormatter.BASIC_ISO_DATE).toString();

        List<Integer> grids = new ArrayList<>();
        grids.add(2);
        //grids.add(3);
        //grids.add(4);

        mGridSize   = grids.get(getRandom(0, grids.size()));
        mGridType   = getRandom(0, SamuraiGridType.values().length);
        mSudokuType = 0;

        SudokuTypes type = SudokuTypes.values()[mSudokuType];
        if(type == SudokuTypes.SAMURAI) {
            mGridSize = 3;
        }


        putString(KEY_CREATED_DATE, mCreatedDate);
        putInt(KEY_GRID_SIZE,       mGridSize);
        putInt(KEY_GAME_MODE,       mGameMode);
        putInt(KEY_GRID_TYPE,       mGridType);
        putInt(KEY_SUDOKU_TYPE,     mSudokuType);
    }

    public boolean isCompleted() {
        return getBoolean(KEY_COMPLETED);
    }
    public void setIsCompleted(boolean value) {
        putBoolean(KEY_COMPLETED, value);
    }

    public int getLevel() {
        return getInt(KEY_LEVEL);
    }

    public int getGridSize() {
        return getInt(KEY_GRID_SIZE);
    }

    public int getGridType() {
        return getInt(KEY_GRID_TYPE);
    }

    public int getGameMode() {
        return getInt(KEY_GAME_MODE);
    }

    public int getSudokuType() {
        return getInt(KEY_SUDOKU_TYPE);
    }

    public int getRandom(int min, int max) {
        List<Integer> vals = new ArrayList<>();
        for(int i = min; i < max; i++) {
            vals.add(i);
        }

        Collections.shuffle(vals);
        return vals.get(0);
    }
}
