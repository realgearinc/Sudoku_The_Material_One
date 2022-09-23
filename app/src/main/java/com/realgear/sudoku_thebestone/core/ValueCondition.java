package com.realgear.sudoku_thebestone.core;

import java.util.ArrayList;
import java.util.List;

public class ValueCondition {
    final private Cell[][] mGrid;
    final private List<Cell> mCells;
    final private List<CellBlock> mBlocks;

    //Used For Checking And Highlighting

    public ValueCondition(Cell[][] grid, List<Cell> cells, List<CellBlock> blocks)
    {
        this.mGrid      = grid;
        this.mCells     = cells;
        this.mBlocks    = blocks;
    }

    public List<Cell> getCellsByValue(int value) {
        List<Cell> result = new ArrayList<Cell>();

        for(Cell cell : mCells) {
            if(cell.getValue() == value) {
                result.add(cell);
            }
        }

        return result;
    }

    public List<Cell> checkCondition(Cell cell) {
        List<Cell> result = new ArrayList<Cell>();
        result.addAll(testRow(cell));
        result.addAll(testColumn(cell));
        result.addAll(testBlock(cell));
        return result;
    }

    private List<Cell> testRow(Cell cell) {

        List<Cell> result = new ArrayList<Cell>();
        for(Cell c : mGrid[cell.getX()]) {
            if(c != cell) {
                if(c.getValue() == cell.getValue()) {
                    result.add(c);
                }
            }
        }

        return result;
    }

    private List<Cell> testColumn(Cell cell) {

        List<Cell> result = new ArrayList<Cell>();
        for(Cell[] cells : mGrid) {
            Cell c = cells[cell.getY()];
            if(c != cell) {
                if(c.getValue() == cell.getValue()) {
                    result.add(c);
                }
            }
        }

        return result;
    }

    private List<Cell> testBlock(Cell cell) {
        int block = cell.getBlock();
        return mBlocks.get(block).checkCondition(cell);
    }

}
