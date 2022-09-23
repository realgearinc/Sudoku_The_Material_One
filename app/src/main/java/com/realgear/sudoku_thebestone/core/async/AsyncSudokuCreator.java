package com.realgear.sudoku_thebestone.core.async;

import android.os.AsyncTask;
import android.util.Log;

import com.realgear.sudoku_thebestone.activities.Gameplay;
import com.realgear.sudoku_thebestone.core.Board;
import com.realgear.sudoku_thebestone.core.Sudoku;
import com.realgear.sudoku_thebestone.core.SudokuCreator;
import com.realgear.sudoku_thebestone.core.SudokuTypes;
import com.realgear.sudoku_thebestone.data.sudoku.SudokuData;
import com.realgear.sudoku_thebestone.utils.CreatorType;
import com.realgear.sudoku_thebestone.utils.Level;

public class AsyncSudokuCreator extends AsyncTask<CreatorType, Integer, Boolean> {
    private final static String TAG = AsyncSudokuCreator.class.getSimpleName();

    private final Gameplay                  mGameplay;
    private final Sudoku mSudoku;
    private final SudokuData                mSudokuData;
    private final Level                     mLevel;
    private final SudokuTypes mSudokuType;

    public AsyncSudokuCreator(Gameplay gameplay,Sudoku mSudoku, SudokuData mSudokuData, Level mLevel, SudokuTypes mSudokuType) {
        this.mGameplay      = gameplay;
        this.mSudoku        = mSudoku;
        this.mSudokuData    = mSudokuData;
        this.mLevel         = mLevel;
        this.mSudokuType    = mSudokuType;
    }

    @Override
    protected Boolean doInBackground(CreatorType... creatorTypes) {
        CreatorType type = creatorTypes[0];

        Log.e(TAG, "Sudoku Creator Is Starting");

        switch (type) {
            case NEW_GAME:
                return SudokuCreator.create(
                        mSudoku,
                        mSudokuData,
                        mLevel,
                        mSudokuType);
            case LOAD_GAME:
                return SudokuCreator.load(
                        mSudoku,
                        mSudokuData
                );
        }

        return false;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        for(Board b : mSudoku.getBoards()) {
            //Log.e(TAG, b.toString());
        }
        mGameplay.onSudokuCreated(aBoolean);
    }
}
