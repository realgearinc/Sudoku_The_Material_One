package com.realgear.sudoku_thebestone.core.sudokutypes.grids;

import com.realgear.sudoku_thebestone.core.Cell;
import com.realgear.sudoku_thebestone.core.sudokutypes.SamuraiGrid;

import java.util.ArrayList;

public class Grid_Super extends SamuraiGrid {

    public Grid_Super(int grid) {
        SIZE = grid;
        RULE_SPACE = (grid == 4) ? 8 : grid;
        SQUARE_SIZE = (grid * grid);

        MAX_LIMIT_X = (SQUARE_SIZE * 3) + (RULE_SPACE * 2);
        MAX_LIMIT_Y = (SQUARE_SIZE * 3) + (RULE_SPACE * 2);

        this.mBoards = new ArrayList<>();
        this.mCells = new ArrayList<>(MAX_LIMIT_X * MAX_LIMIT_Y);
        this.mGrid = new Cell[MAX_LIMIT_X][MAX_LIMIT_Y];

        initCells();
        setINACTIVE();
    }

    @Override
    public void initCells() {
        createGrid(0, 0);
        createGrid(0, SQUARE_SIZE + RULE_SPACE);
        createGrid(0, (SQUARE_SIZE + RULE_SPACE) * 2);

        createGrid(6, 6);
        createGrid(6, 6 + SQUARE_SIZE + RULE_SPACE);

        createGrid(SQUARE_SIZE + RULE_SPACE, 0);
        createGrid(SQUARE_SIZE + RULE_SPACE, SQUARE_SIZE + RULE_SPACE);
        createGrid(SQUARE_SIZE + RULE_SPACE, (SQUARE_SIZE + RULE_SPACE) * 2);

        createGrid(SQUARE_SIZE + RULE_SPACE + 6, 6);
        createGrid(SQUARE_SIZE + RULE_SPACE + 6, 6 + SQUARE_SIZE + RULE_SPACE);

        createGrid((SQUARE_SIZE + RULE_SPACE) * 2, 0);
        createGrid((SQUARE_SIZE + RULE_SPACE) * 2, SQUARE_SIZE + RULE_SPACE);
        createGrid((SQUARE_SIZE + RULE_SPACE) * 2, (SQUARE_SIZE + RULE_SPACE) * 2);
    }
}
