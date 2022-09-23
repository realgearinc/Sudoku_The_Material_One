package com.realgear.sudoku_thebestone.data.sudoku;

import android.content.Context;
import android.util.Log;

import com.realgear.sudoku_thebestone.core.GameType;
import com.realgear.sudoku_thebestone.core.SudokuTypes;
import com.realgear.sudoku_thebestone.utils.SamuraiGridType;

public class SudokuData {
    private final static String TAG = SudokuData.class.getSimpleName();

    private final BoardData mBoardData;
    private boolean isSaved = false;

    public SudokuData(Context context, SudokuTypes sudokuType) {
        String key = sudokuType.name();

        this.mBoardData = new BoardData(context, key);
        this.isSaved = mBoardData.getIsSaved();
    }

    public SudokuData(Context context, SamuraiGridType gridType, GameType gameType, SudokuTypes sudokuType) {
        String key = (sudokuType != SudokuTypes.SAMURAI) ? (gameType.name() + "_" + sudokuType.name()) : (gridType.name() + "_" + gameType.name() + "_" + sudokuType.name());

        this.mBoardData = new BoardData(context, key);
        this.isSaved = mBoardData.getIsSaved();
    }

    public boolean isSaved() {
        return this.mBoardData.getIsSaved();
    }

    public BoardData getBoardData() {
        return this.mBoardData;
    }

    public void Save() {
        if(isSaved)
            return;

        mBoardData.setIsSaved(true);
        this.isSaved = true;

        Log.e(TAG, "Game Is Saved");
    }
}
