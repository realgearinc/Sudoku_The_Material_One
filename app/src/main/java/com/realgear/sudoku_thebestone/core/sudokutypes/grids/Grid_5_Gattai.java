package com.realgear.sudoku_thebestone.core.sudokutypes.grids;

import com.realgear.sudoku_thebestone.core.Cell;
import com.realgear.sudoku_thebestone.core.sudokutypes.SamuraiGrid;

import java.util.ArrayList;

public class Grid_5_Gattai extends SamuraiGrid {

    public Grid_5_Gattai(int grid) {
        SIZE = grid;
        RULE_SPACE = (grid == 4) ? 8 : grid;
        SQUARE_SIZE = (grid * grid);

        MAX_LIMIT_X = SQUARE_SIZE + (RULE_SPACE * 2);
        MAX_LIMIT_Y = ((SQUARE_SIZE + RULE_SPACE) * 2) + RULE_SPACE;

        this.mBoards = new ArrayList<>();
        this.mCells = new ArrayList<>(MAX_LIMIT_X * MAX_LIMIT_Y);
        this.mGrid = new Cell[MAX_LIMIT_X][MAX_LIMIT_Y];

        initCells();
        setINACTIVE();
    }

    @Override
    public void initCells() {
        createGrid(0, 0);
        createGrid(RULE_SPACE, RULE_SPACE);
        createGrid(RULE_SPACE * 2, RULE_SPACE * 3);
        createGrid(RULE_SPACE, SQUARE_SIZE + (RULE_SPACE * 2));
        createGrid(0, SQUARE_SIZE * 2);
    }
}
