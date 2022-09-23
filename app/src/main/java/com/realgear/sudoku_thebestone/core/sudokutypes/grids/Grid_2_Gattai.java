package com.realgear.sudoku_thebestone.core.sudokutypes.grids;

import com.realgear.sudoku_thebestone.core.Cell;
import com.realgear.sudoku_thebestone.core.sudokutypes.SamuraiGrid;

import java.util.ArrayList;

public class Grid_2_Gattai extends SamuraiGrid {
    public Grid_2_Gattai(int grid) {
        SIZE = grid;
        RULE_SPACE = (grid == 4) ? 8 : grid;
        SQUARE_SIZE = (grid * grid);

        MAX_LIMIT_X = SQUARE_SIZE + 6;
        MAX_LIMIT_Y = SQUARE_SIZE + 3;

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
        startY = 3;

        createGrid(startX, startY);
    }
}
