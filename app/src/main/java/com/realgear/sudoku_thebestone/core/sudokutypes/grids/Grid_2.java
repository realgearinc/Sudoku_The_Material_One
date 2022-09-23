package com.realgear.sudoku_thebestone.core.sudokutypes.grids;

import com.realgear.sudoku_thebestone.core.Cell;
import com.realgear.sudoku_thebestone.core.sudokutypes.SamuraiGrid;

import java.util.ArrayList;

public class Grid_2 extends SamuraiGrid {

    public Grid_2(int grid) {
        SIZE = grid;
        RULE_SPACE = (grid == 4) ? 8 : grid;
        SQUARE_SIZE = (grid * grid);
        MAX_LIMIT_X = SQUARE_SIZE + ((grid == 4) ? 2 : 6);
        MAX_LIMIT_Y = SQUARE_SIZE + ((grid == 4) ? 2 : 6);

        switch (SQUARE_SIZE) {
            case 4:
                RULE_BORDER = 3;
                break;
            case 9:
                RULE_BORDER = 6;
        }

        this.mBoards    = new ArrayList<>();
        this.mCells     = new ArrayList<>(MAX_LIMIT_X * MAX_LIMIT_Y);
        this.mGrid      = new Cell[MAX_LIMIT_X][MAX_LIMIT_Y];

        initCells();
        setINACTIVE();
    }

    @Override
    public void initCells() {
        int startX = 0;
        int startY = 0;

        createGrid(startX, startY);

        startY = RULE_BORDER;
        startX = RULE_BORDER;

        createGrid(startX, startY);
    }
}
