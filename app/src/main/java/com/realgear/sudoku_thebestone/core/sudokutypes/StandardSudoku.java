package com.realgear.sudoku_thebestone.core.sudokutypes;

import com.realgear.sudoku_thebestone.core.Board;
import com.realgear.sudoku_thebestone.core.Cell;
import com.realgear.sudoku_thebestone.core.Sudoku;
import com.realgear.sudoku_thebestone.core.SudokuType;

import java.util.ArrayList;
import java.util.List;

public class StandardSudoku extends SudokuType {
    private final static String TAG = StandardSudoku.class.getSimpleName();

    private final int SIZE;
    private final int MAX_LIMIT;
    private final int SQUARE_SIZE;

    private final Cell[][]      mGrid;
    private final List<Cell>    mCells;
    private final List<Board>   mBoards;

    public StandardSudoku(int gridSize) {
        this.SIZE = gridSize;
        this.SQUARE_SIZE = SIZE * SIZE;
        this.MAX_LIMIT = SQUARE_SIZE;

        this.mGrid = new Cell[MAX_LIMIT][MAX_LIMIT];
        this.mCells = new ArrayList<>(MAX_LIMIT * MAX_LIMIT);
        this.mBoards = new ArrayList<>();

        createGrid();
    }

    public void createGrid() {
        for(int x = 0; x < MAX_LIMIT; x++) {
            for(int y = 0; y < MAX_LIMIT; y++) {
                Cell cell = new Cell(x, y, SIZE, true);
                cell.setValue(0);
                mGrid[x][y] = cell;
                mCells.add(cell);
            }
        }

        mBoards.add(new Board(mGrid, mCells, SIZE, SQUARE_SIZE, 0, 0));
    }

    @Override
    public Sudoku getSudoku() {
        return new Sudoku(this.mCells, this.mGrid, this.mBoards, this.MAX_LIMIT, this.MAX_LIMIT);
    }
}
