package com.realgear.sudoku_thebestone.core.sudokutypes;

import com.realgear.sudoku_thebestone.core.Board;
import com.realgear.sudoku_thebestone.core.Cell;

import java.util.List;

public class SamuraiGrid {
    public Cell[][]       mGrid;
    public List<Cell>     mCells;
    public List<Board>    mBoards;

    public int RULE_SPACE;
    public int MAX_LIMIT_X;
    public int MAX_LIMIT_Y;
    public int SIZE;
    public int SQUARE_SIZE;
    public int RULE_BORDER;

    public SamuraiGrid() {

    }

    public void initCells() {

    }

    public void setINACTIVE() {
        for(int x = 0; x < MAX_LIMIT_X; x++) {
            for(int y = 0; y < MAX_LIMIT_Y; y++) {
                if(mGrid[x][y] == null) {
                    mGrid[x][y] = new Cell(x, y, SIZE, false);
                }
            }
        }
    }

    public void createGrid(int startX, int startY) {
        for(int x = startX; x < (startX + SQUARE_SIZE); x++) {
            for(int y = startY; y < (startY + SQUARE_SIZE); y++) {
                if(mGrid[x][y] != null) {
                    if(mGrid[x][y].isActive()) {
                        continue;
                    }
                }
                else{
                    Cell cell = new Cell(x, y, SIZE, true);
                    cell.setValue(0);
                    mGrid[x][y] = cell;
                    mCells.add(cell);
                }
            }
        }

        mBoards.add(new Board(mGrid, mCells, SIZE, SQUARE_SIZE, startX, startY));
    }

    public Cell[][] getGrid() {
        return mGrid;
    }

    public List<Cell> getCells() {
        return mCells;
    }

    public List<Board> getBoards() {
        return mBoards;
    }
}
