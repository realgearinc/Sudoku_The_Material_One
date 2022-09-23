package com.realgear.sudoku_thebestone.core;

import java.util.ArrayList;
import java.util.List;

public class CellBlock {
    final private List<Cell> mCells;

    public CellBlock(int squareSize) {
        this.mCells = new ArrayList<>();;
    }

    public void add(Cell cell) {
        mCells.add(cell);
        //mGrid[cell.getX()][cell.getY()] = cell;
    }

    public boolean cleanCompare(Cell c, int x, int y) {
        int nX = c.getX() - getTopLeft().getX();
        int nY = c.getY() - getTopLeft().getY();

        return (nX == x && nY == y);
    }

    public boolean cleanCompareX(Cell c, int x) {
        int nX = c.getX() - getTopLeft().getX();

        return nX == x;
    }
    public boolean cleanCompareY(Cell c, int y) {
        int nY = c.getY() - getTopLeft().getY();

        return nY == y;
    }

    public Cell getTopLeft() {
        Cell tempCell = null;

        for(Cell cell : mCells) {
            if(tempCell == null) {
                tempCell = cell;
            }
            else {
                if(cell.getX() < tempCell.getX()) {
                    tempCell = cell;
                }
                else if(cell.getY() < tempCell.getY()) {
                    tempCell = cell;
                }
            }
        }

        return tempCell;
    }

    public Cell getTopRight() {
        //Cell tempCell = null;

        int x = -1;
        int y = -1;

        for(Cell cell : mCells) {
            if(x == -1 && y == -1) {
                x = cell.getX();
                y = cell.getY();
            }
            else {
                if (cell.getX() < x) {
                    x = cell.getX();
                }
                if(cell.getY() > y) {
                    y = cell.getY();
                }
            }
        }

        return getCell(x, y);
    }

    public Cell getLowerLeft() {
        Cell tempCell = null;

        for(Cell cell : mCells) {
            if(tempCell == null) {
                tempCell = cell;
            }
            else {
                if(cell.getX() > tempCell.getX()) {
                    tempCell = cell;
                }
                if(cell.getY() < tempCell.getY()) {
                    tempCell = cell;
                }
            }
        }

        return tempCell;
    }

    public Cell getLowerRight() {
        Cell tempCell = null;

        for(Cell cell : mCells) {
            if(tempCell == null) {
                tempCell = cell;
            }
            else {
                if(cell.getX() > tempCell.getX()) {
                    tempCell = cell;
                }

                if(cell.getY() > tempCell.getY()) {
                    tempCell = cell;
                }
            }
        }

        return tempCell;
    }

    public List<Cell> getCells() {
        return this.mCells;
    }

    public Cell getCell(int x, int y) {
        for(Cell c : mCells) {
            if(c.getX() == x && c.getY() == y) {
                return c;
            }
        }
        return null;
    }

    public boolean hasValue(int value) {
        for(Cell cell : mCells) {
            if(cell.getValue() == value) {
                return true;
            }
        }

        return false;
    }

    public List<Cell> checkCondition(Cell c) {
        List<Cell> result = new ArrayList<Cell>();
        for(Cell cell : mCells) {
            if(cell != c) {
                if(c.getValue() == cell.getValue()) {
                    result.add(cell);
                }
            }
        }

        return result;
    }
}
