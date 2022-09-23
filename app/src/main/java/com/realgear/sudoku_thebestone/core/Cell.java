package com.realgear.sudoku_thebestone.core;

public class Cell implements Comparable {
    private final int mSquareSize;
    private final int mSize;
    private final int x;
    private final int y;

    private boolean isActive;

    private boolean isExtra;

    private int mValue;
    private int mSolution;
    private int mSave;

    private int mBlock;

    private CellState.State mCellState;
    private CellRule mCellRule;

    private Hints mHints;

    public Cell(int x, int y, int size, boolean active) {
        this.x = x;
        this.y = y;
        this.mSize = size;
        this.mSquareSize = mSize * mSize;
        this.isActive = active;
    }

    public int getBlock() { return this.mBlock; }

    public int getX() { return this.x; }
    public int getY() { return this.y; }

    public int getValue() { return this.mValue; }
    public int getSolution() { return this.mSolution; }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }
    public boolean isActive() { return this.isActive; }

    public boolean isBlank() {
        return (mCellState == CellState.State.BLANK);
    }
    public boolean isFixed() {
        return (mCellState == CellState.State.FIXED);
    }

    public void setFixed() {
        if(mCellState == CellState.State.FILLED) {
            mCellState = CellState.State.FIXED;
        }
    }

    public void setIsExtra(boolean value) {
        this.isExtra = value;
    }

    public boolean isExtra() {
        return this.isExtra;
    }

    public void setValue(int value) {
        this.mValue = value;

        mCellState = (value == 0) ? CellState.State.BLANK : CellState.State.FILLED;
    }

    public void setCellRule(CellRule cellRule) {
        this.mCellRule = cellRule;
    }

    public void setSolution() {
        this.mSolution = this.mValue;
    }

    public void setBlock(int block) {
        this.mBlock = block;
    }

    public CellRule getCellRule() {
        return this.mCellRule;
    }

    public void setHint(Hints hints) {
        this.mHints = hints;
    }

    public Hints getHints() {
        return this.mHints;
    }

    @Override
    public int compareTo(Object o) {
        Cell alt = null;
        try {
            alt = (Cell) o;
        }
        catch (Exception e) {
            return 0;
        }
        boolean xEqual = (this.x == alt.getX());
        boolean yEqual = (this.y == alt.getY());

        return (xEqual && yEqual) ? 1 : 0;
    }
}
