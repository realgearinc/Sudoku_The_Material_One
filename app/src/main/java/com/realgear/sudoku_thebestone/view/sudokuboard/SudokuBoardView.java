package com.realgear.sudoku_thebestone.view.sudokuboard;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import androidx.gridlayout.widget.GridLayout;

import com.otaliastudios.zoom.ZoomLayout;
import com.realgear.sudoku_thebestone.core.ActionState;
import com.realgear.sudoku_thebestone.core.async.AsyncHintsCreator;
import com.realgear.sudoku_thebestone.core.Cell;
import com.realgear.sudoku_thebestone.core.async.AsyncGridLoader;
import com.realgear.sudoku_thebestone.core.Sudoku;

import com.realgear.sudoku_thebestone.core.CellState;
import com.realgear.sudoku_thebestone.core.Undo;
import com.realgear.sudoku_thebestone.core.enums.HintsCreator;
import com.realgear.sudoku_thebestone.theme.Theme;
import com.realgear.sudoku_thebestone.theme.Themeable;
import com.realgear.sudoku_thebestone.view.GameplayView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SudokuBoardView extends Themeable {
    private static final String TAG = SudokuBoardView.class.getSimpleName();

    private Theme mTheme;

    //UI Properties
    private final ZoomLayout    mZoomLayout;
    private final GridLayout    mGridLayout;
    private final Context       mContext;
    private final GameplayView  mGameplayView;

    //Core Properties
    private AsyncHintsCreator mHintsCreator;

    //UI Fields
    private float mBoardHeight;
    private float mBoardWidth;

    private boolean isFlingEnabled;
    private boolean isTwoFingersScrollEnabled;
    private boolean isHorizontalPanEnabled;
    private boolean isVerticalPanEnabled;
    private boolean isOverPinchableEnabled;
    private boolean isZoomEnabled;
    private float mMinZoom;
    private float mMaxZoom;



    //Core Properties
    private Sudoku       mSudoku;
    private CellView[][] mCells;

    private boolean mShowHints;

    private ActionState mActionState;
    private int         mActionValue;

    private int mLastValue = -1;

    private List<Undo> mUndo = new ArrayList<>();

    public SudokuBoardView(ZoomLayout zoomLayout, GridLayout gridLayout, Context context, GameplayView gameplayView) {
        this.mZoomLayout    = zoomLayout;
        this.mGridLayout    = gridLayout;
        this.mContext       = context;
        this.mGameplayView  = gameplayView;
        this.mHintsCreator  = new AsyncHintsCreator(this);
        this.mShowHints     = false;

        isFlingEnabled = true;
        isTwoFingersScrollEnabled = true;
        isHorizontalPanEnabled = true;
        isVerticalPanEnabled = true;
        isOverPinchableEnabled = true;
        isZoomEnabled = true;
        mMinZoom = 0.98F;
        mMaxZoom = 5;

        mActionState = ActionState.ADD;

        initBoardLayout();
        initZoomLayout();
    }

    public void createBoard(Sudoku sudoku) {
        mSudoku = sudoku;
        if(mSudoku == null)
            return;

        mGridLayout.removeAllViews();

        mGridLayout.setColumnCount(mSudoku.getMaxLimitY());
        mGridLayout.setRowCount(mSudoku.getMaxLimitX());
        mGridLayout.setUseDefaultMargins(false);

        mCells = new CellView[mSudoku.getMaxLimitX()][mSudoku.getMaxLimitY()];
        Cell[][] grid = mSudoku.getGrid();

        AsyncGridLoader gridLoader = new AsyncGridLoader(mContext, this);

        gridLoader.execute(grid);
    }

    public void addView(Object obj) {
        CellView cellView   = null;
        View     view       = null;

        try {
            cellView = (CellView)obj;
        }
        catch (Exception e) {}
        try {
            view = (View) obj;
        }
        catch (Exception e) {}

        if(cellView != null) {
            mCells[cellView.getX()][cellView.getY()] = cellView;
        }

        CellView finalCellView = cellView;
        View finalView = view;
        mGridLayout.post(new Runnable() {
            @Override
            public void run() {
                mGridLayout.addView((finalCellView == null) ? finalView : finalCellView.getView(), 60, 60);
                if(finalCellView != null) {
                    finalCellView.initStyle();
                    if(mTheme != null) {
                        finalCellView.setTheme(mTheme);
                    }
                }
            }
        });
    }

    public void onClickCellView(int x, int y) {

        //Cell cell = mSudokuBoard.getCell(x, y);
        Cell cell = mSudoku.getCell(x, y);

        if(cell == null || cell.isFixed())
            return;

        int oldVal = cell.getValue();
        int curVal = cell.getValue();

        int countValue = cell.getValue();

        if (curVal + 1 > mSudoku.getBoardSquareSize()) {
            curVal = 1;
        } else {
            curVal++;
        }

        if (mActionState == ActionState.ADD_HIGHLIGHT && mActionValue > 0) {
            curVal = mActionValue;
            mCells[x][y].setCellStatus(CellState.Status.HIGHLIGHTED);
        }

        if(mActionState == ActionState.REMOVE) {
            curVal = 0;
        }

        mUndo.add(new Undo(cell.getX(), cell.getY(), oldVal));

        cell.setValue(curVal);
        mCells[x][y].setValue(curVal);

        if (countValue != 0) {
            mGameplayView.updateCount(countValue);
        }
        if (curVal != 0 && curVal != countValue) {
            mGameplayView.updateCount(curVal);
        }

        if(mShowHints)
            showHints(cell);

        checkSudoku();
    }

    public void onLongClickCellView(int x, int y) {
        Cell cell = mSudoku.getCell(x, y);
        if(cell == null || cell.isFixed() || cell.isExtra() || cell.isBlank())
            return;

        int oldValue = cell.getValue();

        mUndo.add(new Undo(x, y, oldValue));

        cell.setValue(0);

        if (oldValue != 0) {
            mGameplayView.updateCount(oldValue);
        }
    }

    public void checkSudoku() {
        if(mSudoku.checkBoard()) {
            //Show Finish Game
            Log.e(TAG, "Finish Game");

            mGameplayView.getActivity().mSudokuData.getBoardData().setCompleted();
            mGameplayView.getActivity().saveLevel();
            mGameplayView.showFinish();
        }
    }

    private void initZoomLayout() {
        if(mZoomLayout == null)
            return;

        mZoomLayout.setFlingEnabled(isFlingEnabled);
        mZoomLayout.setTwoFingersScrollEnabled(isTwoFingersScrollEnabled);
        mZoomLayout.setHorizontalPanEnabled(isHorizontalPanEnabled);
        mZoomLayout.setVerticalPanEnabled(isVerticalPanEnabled);
        mZoomLayout.setOverPinchable(isOverPinchableEnabled);
        mZoomLayout.setZoomEnabled(isZoomEnabled);
        mZoomLayout.setMinZoom(mMinZoom);
        mZoomLayout.setMaxZoom(mMaxZoom);

        //mZoomLayout.setBackgroundResource(R.drawable.grid_secondary);
        mZoomLayout.setClipToOutline(false);
        //mZoomLayout.setPadding(2,2,2,2);
    }

    private void initBoardLayout() {
        DisplayMetrics metrics = new DisplayMetrics();
        mGameplayView.getActivity().getWindow().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels;
        //int width = mGameplayView.getActivity().getWindow().getWindowManager().getCurrentWindowMetrics().getBounds().width();
        setBoardHeight(width);
    }

    public int getMaxLimitX() {
        return this.mSudoku.getMaxLimitX();
    }

    public int getMaxLimitY() {
        return this.mSudoku.getMaxLimitY();
    }

    public void setBoardHeight(int height) {
        if(mZoomLayout == null)
            return;

        this.mBoardHeight = height;
        mZoomLayout.getLayoutParams().height = height;
        Log.e(TAG, "Set Height : " + height);
    }

    public void setActionState(ActionState actionState, int value) {
        this.mActionState = actionState;
        this.mActionValue = value;
        switch (mActionState) {
            case ADD_HIGHLIGHT:
                highlightValue(mActionValue);
                break;
            case ADD:
                removeHighlights();
                break;
        }
    }

    public void Undo() {
        if(mUndo.size() > 0) {
            Undo toUndo = mUndo.get(mUndo.size()-1);

            mCells[toUndo.getX()][toUndo.getY()].setValue(toUndo.getValue());
            mSudoku.getCell(toUndo.getX(), toUndo.getY()).setValue(toUndo.getValue());

            mUndo.remove(toUndo);
        }
    }

    public void highlightValue(int value) {
        for(CellView[] c : mCells) {
            for(CellView cell : c) {
                if(cell == null)
                    continue;

                if(cell.getValue() == value) {
                    cell.setCellStatus(CellState.Status.HIGHLIGHTED);
                }
                else {
                    cell.setCellStatus(CellState.Status.NORMAL);
                }
            }
        }
    }

    public void removeHighlights() {
        for(CellView[] c : mCells) {
            for(CellView cell : c) {
                if(cell == null)
                    continue;

                cell.setCellStatus(CellState.Status.NORMAL);
            }
        }
    }

    @Override
    public Theme getTheme() {
        return this.mTheme;
    }

    @Override
    public void setTheme(Theme theme) {
        this.mTheme = theme;
        updateTheme();
    }

    @Override
    public void updateTheme() {
        //new AsyncThemeChanger(mCells).execute(this.mTheme);

        for(CellView[] c : mCells) {
            for(CellView cell : c) {
                if(cell == null)
                    continue;

                cell.setTheme(this.mTheme);
            }
        }

    }


    public GameplayView getGameplayView() {
        return this.mGameplayView;
    }

    public void showHints(Cell cell) {
        if(!cell.isActive() || cell.isExtra() || !cell.isBlank())
            return;

        mCells[cell.getX()][cell.getY()].setHints(refreshHints(cell));
    }

    public void setHints(HintsCreator creatorType) {
        if(creatorType == null)
            return;

        //mHintsCreator.cancel(true);

        mHintsCreator = new AsyncHintsCreator(this);

        mHintsCreator.setCreatorType(creatorType);
        mHintsCreator.execute(mCells);
    }

    public Cell getCell(int x, int y) {
        return mSudoku.getCell(x, y);
    }

    public List<Integer> refreshHints(Cell cell) {
        return mSudoku.recheckHints(cell);
    }

    public void setHintsEnabled(Boolean value) {
        this.mShowHints = value;
    }

    public void validateBoard() {
        Log.e(TAG, "Validating Board");

        try {
            HashMap<Cell, CellState.Status> result = mSudoku.validateBoard();

            if (result.size() == 0) {
                Log.e(TAG, "No Error & Solved Values");
            } else {
                Log.e(TAG, "Found Error & Solved Values");
            }

            for (Cell cell : result.keySet()) {
                CellView cellView = mCells[cell.getX()][cell.getY()];
                cellView.setCellStatus(result.get(cell));
            }
        }
        catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    public int getSquareSize() {
        return mSudoku.getBoardSquareSize();
    }

    public int getSize() {
        return mSudoku.getSize();
    }
}
