package com.realgear.sudoku_thebestone.core.async;

import android.os.AsyncTask;
import android.util.Log;

import com.realgear.sudoku_thebestone.core.Cell;
import com.realgear.sudoku_thebestone.core.enums.HintsCreator;
import com.realgear.sudoku_thebestone.view.sudokuboard.CellView;
import com.realgear.sudoku_thebestone.view.sudokuboard.SudokuBoardView;

public class AsyncHintsCreator extends AsyncTask<CellView[][], Void, Boolean> {
    private final SudokuBoardView mSudokuBoardView;
    private HintsCreator mCreatorType;

    public AsyncHintsCreator(SudokuBoardView sudokuBoardView) {
        this.mSudokuBoardView = sudokuBoardView;
    }

    public void setCreatorType(HintsCreator creatorType) {
        this.mCreatorType = creatorType;
    }

    @Override
    protected void onPostExecute(Boolean value) {
        mSudokuBoardView.setHintsEnabled(value);
        Log.e("AsyncHints", "Is Created : " + value);
    }

    @Override
    protected Boolean doInBackground(CellView[][]... cellViews) {
        CellView[][] cells = cellViews[0];

        boolean result = false;

        int DURATION = 25;

        switch (mCreatorType) {
            case ADD:
                for(int x = 0; x < mSudokuBoardView.getMaxLimitX(); x++) {
                    for(int y = 0; y < mSudokuBoardView.getMaxLimitY(); y++) {
                        if(cells[x][y] == null)
                            continue;

                        CellView cellView = cells[x][y];
                        Cell cell = mSudokuBoardView.getCell(x, y);

                        if(!cell.isActive() || cell.isExtra() || !cell.isBlank())
                            continue;

                        cellView.setHints(mSudokuBoardView.refreshHints(cell));

                        try {
                            Thread.sleep(DURATION);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                result = true;
                break;

            case REMOVE:
                for(int x = 0; x < mSudokuBoardView.getMaxLimitX(); x++) {
                    for(int y = 0; y < mSudokuBoardView.getMaxLimitY(); y++) {
                        if(cells[x][y] == null)
                            continue;

                        CellView cellView = cells[x][y];
                        Cell cell = mSudokuBoardView.getCell(x, y);

                        if(!cell.isActive() || cell.isExtra() || !cell.isBlank())
                            continue;

                        cellView.hideHints();

                        try {
                            Thread.sleep(DURATION);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                result = false;
                break;
        }

        return result;
    }
}
