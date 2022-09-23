package com.realgear.sudoku_thebestone.core;

import java.util.ArrayList;
import java.util.List;

public class CheckCondition {

    final private Board mBoard;

    final private Cell[][] mGrid;
    final private List<Cell> mCells;
    final private List<CellBlock> mBlocks;

    final private int mSquareSize;
    final private int mOffsetX;
    final private int mOffsetY;

    public CheckCondition(Board board, Cell[][] grid, List<Cell> cells, List<CellBlock> blocks, int squareSize, int offsetX, int offsetY) {
        this.mBoard = board;
        this.mGrid = grid;
        this.mCells = cells;
        this.mBlocks = blocks;
        this.mSquareSize = squareSize;
        this.mOffsetX = offsetX;
        this.mOffsetY = offsetY;
    }

    public List<Cell> check(Cell cell) {
        List<Cell> result = new ArrayList<>();

        result.addAll(testRow(cell));
        result.addAll(testColumn(cell));
        result.addAll(testBlock(cell));

        return result;
    }

    private List<Cell> testRow(Cell cell) {
        List<Cell> result = new ArrayList<>();

        int x = cell.getX();
        for(int y = mOffsetY; y < (mOffsetY + mSquareSize); y++) {
            if(mGrid[x][y] != cell) {
                if(mGrid[x][y].getValue() == cell.getValue()) {
                    result.add(mGrid[x][y]);
                }
            }
        }

        return result;
    }

    private List<Cell> testColumn(Cell cell) {
        List<Cell> result = new ArrayList<>();

        int y = cell.getY();
        for(int x = mOffsetX; x < (mOffsetX + mSquareSize); x++) {
            if(mGrid[x][y] != cell) {
                if(mGrid[x][y].getValue() == cell.getValue()) {
                    result.add(mGrid[x][y]);
                }
            }
        }

        return result;
    }

    private List<Cell> testBlock(Cell cell) {
        int block = mBoard.getBlockByPos(cell.getX(), cell.getY());
        return mBlocks.get(block).checkCondition(cell);
    }
}
