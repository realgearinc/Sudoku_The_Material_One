package com.realgear.sudoku_thebestone.core.sudokutypes.grids;

import com.realgear.sudoku_thebestone.core.Cell;
import com.realgear.sudoku_thebestone.core.sudokutypes.SamuraiGrid;

import java.util.ArrayList;

public class Grid_3 extends SamuraiGrid {

    public Grid_3(int grid) {
        SIZE = grid;
        RULE_SPACE = (grid == 4) ? 8 : grid;
        SQUARE_SIZE = (grid * grid);

        MAX_LIMIT_X = (SQUARE_SIZE * 2) + RULE_SPACE;
        MAX_LIMIT_Y = (SQUARE_SIZE + 6);

        this.mBoards = new ArrayList<>();
        this.mCells = new ArrayList<>(MAX_LIMIT_X * MAX_LIMIT_Y);
        this.mGrid = new Cell[MAX_LIMIT_X][MAX_LIMIT_Y];

        initCells();
        setINACTIVE();
    }

    @Override
    public void initCells() {
        int startX = 0;
        int startY = 0;

        createGrid(startX, startY);

        startX = 6;
        startY = 6;

        createGrid(startX, startY);

        startX = SQUARE_SIZE + RULE_SPACE;
        startY = 0;

        createGrid(startX, startY);
    }
}
