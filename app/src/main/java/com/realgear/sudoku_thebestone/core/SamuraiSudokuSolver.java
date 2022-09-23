package com.realgear.sudoku_thebestone.core;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SamuraiSudokuSolver {
    private Sudoku mSudoku;

    private int mCount;
    private int mIndex;

    private int mSquareSize;

    public SamuraiSudokuSolver setSudoku(Sudoku sudoku) {
        this.mSudoku        = sudoku;
        this.mSquareSize    = mSudoku.getBoardSquareSize();
        this.mCount = 0;
        this.mIndex = 0;

        return this;
    }

    public int solve(int limit) {
        Log.e("SudokuSolver", "Solving Sudoku");
        Log.e("SudokuSolver", "Square Size : " + mSquareSize);
        mCount = 0;
        mIndex = 0;

        int result = 0;

        for(int i = 0; i < mSudoku.getBoards().size(); i++) {
            Board board = mSudoku.getBoards().get(i);

            mCount = 0;
            mIndex = 0;

            List<Cell> blanksCells = board.getBlankCells();
            Log.e("SudokuSolver", "Blank Cells Size : " + blanksCells.size());
            //Collections.shuffle(blanksCells);

            result = gen(blanksCells, limit);

            if(result == 0) {
                for(int j = i; j > -1; j--) {
                    Board b = mSudoku.getBoards().get(j);
                    b.setCellsBlank();
                }

                i = -1;
            }

        }

        /*else {
            List<Cell> blankCells = mSudoku.getBlankCells();
            result = gen(blankCells, limit);
        }*/
        Log.e("SudokuSolver", "Solved Sudoku");
        return result;
    }

    public int gen(List<Cell> blankCells, int limit) {
        if(mIndex < (blankCells.size())) {
            for(int i : randomOrderDigits(mSquareSize)) {
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
                break;
            }
        }

        if(isValid) {
            cell.setValue(i);
            if(cell.isExtra()) {
                //Log.e("Solver", "Extra Cell Was Found");
            }
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
