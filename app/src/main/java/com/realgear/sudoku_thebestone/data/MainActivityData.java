package com.realgear.sudoku_thebestone.data;

import android.content.Context;

public class MainActivityData extends DataPref {
    private final String KEY_SELECTED_THEME     = "SELECTED_THEME";
    private final String KEY_SELECTED_LEVEL     = "SELECTED_LEVEL";
    private final String KEY_SELECTED_SUDOKU    = "SELECTED_SUDOKU";
    private final String KEY_SELECTED_GRID      = "SELECTED_GRID";

    private final String KEY_FIRST_RUN = "FIRST_RUN";

    public MainActivityData(Context context) {
        super(context, MainActivityData.class.getSimpleName());
    }

    public boolean isFirstRun() {
        return !getBoolean(KEY_FIRST_RUN);
    }

    public void setFirstRun() {
        putBoolean(KEY_FIRST_RUN, true);
    }

    public int getLastSelectedTheme() {
        //Log.e("MainActivityView", "Last Saved Theme : " + getInt(KEY_SELECTED_THEME));
        return getInt(KEY_SELECTED_THEME);
    }

    public int getLastSelectedLevel() {
        return getInt(KEY_SELECTED_LEVEL);
    }

    public int getLastSelectedSudoku() {
        return getInt(KEY_SELECTED_SUDOKU);
    }

    public int getLastSelectedGrid() {
        return getInt(KEY_SELECTED_GRID);
    }

    public void setLastSelectedTheme(int value) {
        //Log.e("MainActivityView", "Last Selected Theme : " + value);
        putInt(KEY_SELECTED_THEME, value);
    }

    public void setLastSelectedLevel(int value) {
        //Log.e("MainActivityView", "Last Selected Level : " + value);
        putInt(KEY_SELECTED_LEVEL, value);
    }

    public void setLastSelectedSudoku(int value) {
        //Log.e("MainActivityView", "Last Selected Type : " + value);
        putInt(KEY_SELECTED_SUDOKU, value);
    }

    public void setLastSelectedGrid(int value) {
        putInt(KEY_SELECTED_GRID, value);
    }
}
