package com.realgear.sudoku_thebestone.adapters;

import com.realgear.sudoku_thebestone.core.Level;
import com.realgear.sudoku_thebestone.core.enums.LevelEnums;

import java.util.List;

public class LevelDataSource {

    private int mRowsCount;
    private int mColumnsCount;

    private LevelEnums[] mHeaders;
    private List<Level> mLevels;

    public LevelDataSource(List<Level> levels) {
        this.mRowsCount = levels.size();
        this.mColumnsCount = LevelEnums.values().length;

        this.mLevels = levels;
        this.mHeaders = LevelEnums.values();
    }

    public int getRowsCount() {
        return mRowsCount;
    }

    public int getColumnsCount() {
        return mColumnsCount;
    }

    public Object getItemData(int rowIndex, int columnIndex) {
        return mLevels.get(rowIndex).getData(mHeaders[columnIndex]);
    }
}
