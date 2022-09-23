package com.realgear.sudoku_thebestone.core;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SudokuSolver {
    private Sudoku mSudoku;

    private int mCount;
    private int mIndex;
    private int mBoardIndex;


    public SudokuSolver setSudoku(Sudoku sudoku) {
        this.mSudoku = sudoku;
        mCount = 0;
        mIndex = 0;

        return this;
    }

    public int solve(int limit) {
        Log.e("SudokuSolver", "Solving Sudoku");
        mBoardIndex = 0;

        mCount = 0;
        mIndex = 0;

        return gen(mSudoku.getBlankCells(), limit);
    }

    public int gen(List<Cell> blankCells, int limit) {
        if(mIndex < blankCells.size()) {
            for(int i : randomOrderDigits(mSudoku.getBoards().get(mBoardIndex).getSquareSize())) {
                if(testValue(blankCells.get(mIndex), i)) {
                    mIndex++;
                    if(gen(blankCells, limit) >= limit) {
                        return mCount;
                    }
                }
            }
            return backtrace(blankCells);
        }
        else {
            return finish();
        }
    }

    private int finish() {
        mCount++;
        mIndex--;
        return mCount;
    }

    private int backtrace(List<Cell> blankCells) {
        blankCells.get(mIndex).setValue(0);
        mIndex--;
        return mCount;
    }

    private boolean testValue(Cell cell, int i) {
        boolean isValid = true;

        List<Board> boards = mSudoku.getLinkedBoards(cell);

        for(Board board : boards) {
            if(!board.testConditions(cell, i)) {
                isValid = false;
            }
        }

        if(isValid) {
            cell.setValue(i);
            return true;
        }
        else {
            return false;
        }
    }

    private List<Integer> randomOrderDigits(int squareSize) {
        List<Integer> values = new ArrayList<Integer>();
        for(int i = 1; i <= squareSize; i++) {
            values.add(i);
        }
        Collections.shuffle(values);
        return values;
    }
}
