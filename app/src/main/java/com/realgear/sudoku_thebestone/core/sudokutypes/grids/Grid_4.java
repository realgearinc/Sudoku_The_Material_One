package com.realgear.sudoku_thebestone.core.sudokutypes.grids;

import com.realgear.sudoku_thebestone.core.Cell;
import com.realgear.sudoku_thebestone.core.sudokutypes.SamuraiGrid;

import java.util.ArrayList;

public class Grid_4 extends SamuraiGrid {

    public Grid_4(int grid) {
        SIZE = grid;
        RULE_SPACE = (grid == 4) ? 8 : grid;
        SQUARE_SIZE = (grid * grid);

        MAX_LIMIT_X = (SQUARE_SIZE * 2) + RULE_SPACE;
        MAX_LIMIT_Y = (SQUARE_SIZE * 2) + RULE_SPACE;

        this.mBoards = new ArrayList<>();
        this.mCells = new ArrayList<>(MAX_LIMIT_X * MAX_LIMIT_Y);
        this.mGrid = new Cell[MAX_LIMIT_X][MAX_LIMIT_Y];

        initCells();
        setINACTIVE();
    }

    @Override
    public void initCells() {
        int startX = 0;
        int startY = 6;

        createGrid(0, 6);
        createGrid(6, 0);
        createGrid(6, 3 + SQUARE_SIZE);
        createGrid(3 + SQUARE_SIZE, 6);

        /*startX = 6;
        startY = 0;

        createGrid(startX, startY);

        startX = 6;
        startY = 12;

        createGrid(startX, startY);

        startX = 12;
        startY = 6;

        createGrid(startX, startY);*/
    }
}
