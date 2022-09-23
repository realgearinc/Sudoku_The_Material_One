package com.realgear.sudoku_thebestone.core.sudokutypes.grids;

import com.realgear.sudoku_thebestone.core.Cell;
import com.realgear.sudoku_thebestone.core.sudokutypes.SamuraiGrid;

import java.util.ArrayList;

public class Grid_3_Gattai extends SamuraiGrid {
    public Grid_3_Gattai(int grid) {
        SIZE = grid;
        RULE_SPACE = (grid == 4) ? 8 : grid;
        SQUARE_SIZE = (grid * grid);

        MAX_LIMIT_X = 6 + SQUARE_SIZE;
        MAX_LIMIT_Y = 6 + SQUARE_SIZE;

        this.mBoards = new ArrayList<>();
        this.mCells = new ArrayList<>(MAX_LIMIT_X * MAX_LIMIT_Y);
        this.mGrid = new Cell[MAX_LIMIT_X][MAX_LIMIT_Y];

        initCells();
        setINACTIVE();
    }

    @Override
    public void initCells() {
        createGrid(0, RULE_SPACE);
        createGrid(RULE_SPACE, RULE_SPACE * 2);
        createGrid(RULE_SPACE * 2, 0);
    }
}
