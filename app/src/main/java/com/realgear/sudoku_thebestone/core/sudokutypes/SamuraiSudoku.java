package com.realgear.sudoku_thebestone.core.sudokutypes;

import android.util.Log;

import com.realgear.sudoku_thebestone.core.Board;
import com.realgear.sudoku_thebestone.core.Cell;
import com.realgear.sudoku_thebestone.core.Sudoku;
import com.realgear.sudoku_thebestone.core.SudokuType;
import com.realgear.sudoku_thebestone.utils.SamuraiGridType;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class SamuraiSudoku extends SudokuType {
    public static final String TAG = SamuraiSudoku.class.getSimpleName();

    public TreeMap<SamuraiGridType, SamuraiGrid> mGrids;

    public enum SudokuType {
        STANDARD,
        SUPER
    }

    private final int RULE_SPACE;
    private final int MAX_LIMIT;
    private final int SIZE;
    private final int SQUARE_SIZE;
    private final int RULE_BORDER;

    private final Cell[][] mGrid;
    private final List<Cell> mCells;
    private final List<Board> mBoards;

    private SudokuType mType;

    public SamuraiSudoku(int grid, SudokuType type) {
        SIZE = grid;
        RULE_SPACE = (grid == 4) ?  8 : grid;
        SQUARE_SIZE = (grid * grid);
        MAX_LIMIT = (type == SudokuType.STANDARD) ?
                ((SQUARE_SIZE * 2) + RULE_SPACE) :
                ((SQUARE_SIZE * 3) + RULE_SPACE * 2);

        switch (SQUARE_SIZE) {
            case 4:
                RULE_BORDER = 3;
                break;
            case 9:
                RULE_BORDER = 6;
                break;
            case 16:
                RULE_BORDER = 12;
                break;

            default:
                RULE_BORDER = 5;
        }

        this.mBoards = new ArrayList<>();
        this.mCells = new ArrayList<>(MAX_LIMIT * MAX_LIMIT);
        this.mGrid = new Cell[MAX_LIMIT][MAX_LIMIT];

        mType = type;
        initCells();
        setINACTIVE();
    }

    private void initCells() {
        if(mType == SudokuType.SUPER) {
            int startX = 0;
            int startY = 0;

            createGrid(startX, startY);

            startY = SQUARE_SIZE + RULE_SPACE;
            Log.e(TAG, "Start Position : {" + startX + "}{"+startY+"}");
            createGrid(startX, startY);

            startY += SQUARE_SIZE + RULE_SPACE;
            createGrid(startX, startY);


            startY = RULE_BORDER;
            startX = RULE_BORDER;
            createGrid(startX, startY);
            startY += SQUARE_SIZE + RULE_SPACE;
            createGrid(startX, startY);
            startY = 0;

            startX = SQUARE_SIZE + RULE_SPACE;
            createGrid(startX, startY);
            startY += SQUARE_SIZE + RULE_SPACE;
            createGrid(startX, startY);
            startY += SQUARE_SIZE + RULE_SPACE;
            createGrid(startX, startY);
            startY = 0;

            startY = RULE_BORDER;
            startX = (SQUARE_SIZE + RULE_SPACE) + RULE_BORDER;
            createGrid(startX, startY);
            startY += SQUARE_SIZE + RULE_SPACE;
            createGrid(startX, startY);
            startY = 0;

            startX = (SQUARE_SIZE * 2) + (RULE_SPACE * 2);
            createGrid(startX, startY);
            startY += SQUARE_SIZE + RULE_SPACE;
            createGrid(startX, startY);
            startY += SQUARE_SIZE + RULE_SPACE;
            createGrid(startX, startY);
        }

        if(mType == SudokuType.STANDARD) {
            int startX = 0;
            int startY = 0;

            createGrid(startX, startY);

            startY = SQUARE_SIZE + RULE_SPACE;
            Log.e(TAG, "Start Position : {" + startX + "}{"+startY+"}");
            createGrid(startX, startY);

            //startY += SQUARE_SIZE + RULE_SPACE;
            //createGrid(startX, startY);


            startY = RULE_BORDER;
            startX = RULE_BORDER;
            createGrid(startX, startY);
            //startY += SQUARE_SIZE + RULE_SPACE;
            //createGrid(startX, startY);
            startY = 0;

            startX = SQUARE_SIZE + RULE_SPACE;
            createGrid(startX, startY);
            startY += SQUARE_SIZE + RULE_SPACE;
            createGrid(startX, startY);
            //startY += SQUARE_SIZE + RULE_SPACE;
            //createGrid(startX, startY);
            startY = 0;

            /*
            startY = RULE_BORDER;
            startX = (SQUARE_SIZE + RULE_SPACE) + RULE_BORDER;
            createGrid(startX, startY);
            startY += SQUARE_SIZE + RULE_SPACE;
            createGrid(startX, startY);
            startY = 0;

            startX = (SQUARE_SIZE * 2) + (RULE_SPACE * 2);
            createGrid(startX, startY);
            startY += SQUARE_SIZE + RULE_SPACE;
            createGrid(startX, startY);
            startY += SQUARE_SIZE + RULE_SPACE;
            createGrid(startX, startY);*/
        }
    }

    private void setINACTIVE() {
        for(int x = 0; x < MAX_LIMIT; x++) {
            for(int y = 0; y < MAX_LIMIT; y++) {
                if(mGrid[x][y] == null) {
                    mGrid[x][y] = new Cell(x, y, SIZE, false);
                }
            }
        }
    }

    private void createGrid(int startX, int startY) {
        for(int x = startX; x < (startX + SQUARE_SIZE); x++) {
            for(int y = startY; y < (startY + SQUARE_SIZE); y++) {
                if(mGrid[x][y] != null) {
                    if(mGrid[x][y].isActive()) {
                        continue;
                    }
                }
                else {
                    Cell cell = new Cell(x, y, SIZE, true);
                    cell.setValue(0);
                    mGrid[x][y] = cell;
                    mCells.add(cell);
                }
            }
        }

        mBoards.add(new Board(mGrid, mCells, SIZE, SQUARE_SIZE, startX, startY));
    }

    @Override
    public Sudoku getSudoku() {
        return new Sudoku(this.mCells, this.mGrid, this.mBoards, this.MAX_LIMIT, MAX_LIMIT);
    }
}
