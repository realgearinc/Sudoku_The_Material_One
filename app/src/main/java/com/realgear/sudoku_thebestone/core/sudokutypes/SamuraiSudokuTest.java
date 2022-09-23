package com.realgear.sudoku_thebestone.core.sudokutypes;

import android.util.Log;

import com.realgear.sudoku_thebestone.core.Board;
import com.realgear.sudoku_thebestone.core.Sudoku;
import com.realgear.sudoku_thebestone.core.SudokuType;
import com.realgear.sudoku_thebestone.core.sudokutypes.grids.Grid_2;
import com.realgear.sudoku_thebestone.core.sudokutypes.grids.Grid_2_Gattai;
import com.realgear.sudoku_thebestone.core.sudokutypes.grids.Grid_3;
import com.realgear.sudoku_thebestone.core.sudokutypes.grids.Grid_3_Gattai;
import com.realgear.sudoku_thebestone.core.sudokutypes.grids.Grid_4;
import com.realgear.sudoku_thebestone.core.sudokutypes.grids.Grid_4_Gattai;
import com.realgear.sudoku_thebestone.core.sudokutypes.grids.Grid_5;
import com.realgear.sudoku_thebestone.core.sudokutypes.grids.Grid_5_Gattai;
import com.realgear.sudoku_thebestone.core.sudokutypes.grids.Grid_Super;
import com.realgear.sudoku_thebestone.utils.SamuraiGridType;

public class SamuraiSudokuTest extends SudokuType {

    private final int               mGridSize;
    private final SamuraiGridType   mGridType;
    private final SamuraiGrid       mGrid;

    public SamuraiSudokuTest(SamuraiGridType gridType) {
        Log.e("SamuraiSudokuTest", "Grid Type : " + gridType.name());
        mGridSize = 3;
        mGridType = gridType;

        switch (mGridType) {
            case GRID_3:
                mGrid = new Grid_3(mGridSize);
                break;
            case GRID_4:
                mGrid = new Grid_4(mGridSize);
                break;
            case GRID_5:
                mGrid = new Grid_5(mGridSize);
                break;
            case GRID_2_GATTAI:
                mGrid = new Grid_2_Gattai(mGridSize);
                break;
            case GRID_3_GATTAI:
                mGrid = new Grid_3_Gattai(mGridSize);
                break;
            case GRID_4_GATTAI:
                mGrid = new Grid_4_Gattai(mGridSize);
                break;
            case GRID_5_GATTAI:
                mGrid = new Grid_5_Gattai(mGridSize);
                break;
            case SUPER:
                mGrid = new Grid_Super(mGridSize);
                break;

            default:
                mGrid = new Grid_2(mGridSize);
                break;
        }

        reAlignGrid();
    }

    private void reAlignGrid() {
        //Align Grid
        switch (mGridType) {
            case GRID_3:
                align(Board.MoveType.UP, 1, 3);
                align(Board.MoveType.LEFT, 1, 3);
                align(Board.MoveType.UP, 2, 3);
                break;

            case GRID_4:
                align(Board.MoveType.LEFT, 0, 3);

                align(Board.MoveType.UP, 1, 3);

                align(Board.MoveType.UP, 2, 3);
                align(Board.MoveType.LEFT, 2, 3);

                align(Board.MoveType.UP, 3, 3);
                align(Board.MoveType.LEFT, 3, 3);

                break;
            case GRID_5:
                align(Board.MoveType.LEFT, 1, 3);

                align(Board.MoveType.UP, 2, 3);
                align(Board.MoveType.LEFT, 2, 3);

                align(Board.MoveType.UP, 3, 3);

                align(Board.MoveType.UP, 4, 3);
                align(Board.MoveType.LEFT, 4, 3);
                break;

            case GRID_2_GATTAI:
                align(Board.MoveType.UP, 1, 3);
                align(Board.MoveType.LEFT, 1, 6);
                break;

            case GRID_3_GATTAI:
                align(Board.MoveType.LEFT, 0, 6);

                align(Board.MoveType.LEFT, 1, 3);
                align(Board.MoveType.LEFT, 1, 6);

                align(Board.MoveType.UP, 2, 6);
                break;
            case GRID_4_GATTAI:
                align(Board.MoveType.LEFT, 2, 6);

                align(Board.MoveType.UP, 3, 6);
                break;
            case GRID_5_GATTAI:
                align(Board.MoveType.UP, 1, 6);
                align(Board.MoveType.LEFT, 1, 6);

                align(Board.MoveType.UP, 2, 3);
                align(Board.MoveType.UP, 2, 6);

                align(Board.MoveType.LEFT, 2, 3);

                align(Board.MoveType.UP, 3, 6);

                align(Board.MoveType.LEFT, 4, 6);

                //addExtraVertical(Board.MoveType.LEFT, 6, 0, 3, 2);
                addExtraVertical(6, 3, 2);

                break;


            case SUPER:
                align(Board.MoveType.LEFT, 1, 3);
                align(Board.MoveType.LEFT, 2, 3);

                //2
                align(Board.MoveType.UP, 3, 3);
                align(Board.MoveType.LEFT, 3, 3);

                align(Board.MoveType.UP, 4, 3);
                align(Board.MoveType.LEFT, 4, 3);

                //3
                align(Board.MoveType.UP, 5, 3);

                align(Board.MoveType.UP, 6, 3);
                align(Board.MoveType.LEFT, 6, 3);

                align(Board.MoveType.UP, 7, 3);
                align(Board.MoveType.LEFT, 7, 3);

                //4
                align(Board.MoveType.UP, 8, 3);
                align(Board.MoveType.LEFT, 8, 3);

                align(Board.MoveType.UP, 9, 3);
                align(Board.MoveType.LEFT, 9, 3);

                //5
                align(Board.MoveType.UP, 10, 3);

                align(Board.MoveType.UP, 11, 3);
                align(Board.MoveType.LEFT, 11, 3);

                align(Board.MoveType.UP, 12, 3);
                align(Board.MoveType.LEFT, 12, 3);

                /*
                align(Board.MoveType.UP, 13, 3);
                align(Board.MoveType.LEFT, 13, 3);*/
                break;


            default:
                align(Board.MoveType.UP, 1, 3);
                align(Board.MoveType.LEFT, 1, 3);
        }
    }

    private void align(Board.MoveType moveType, int boardIndex, int index) {
        Board board = mGrid.getBoards().get(boardIndex);
        if(board != null) {
            board.MoveLine(moveType, index);
        }
    }

    private void addExtraVertical(int startX, int index, int boardIndex) {
        Board board = mGrid.mBoards.get(boardIndex);

        board.addExtraVertical(Board.MoveType.LEFT, startX, 0, index);
        board.addExtraVertical(Board.MoveType.RIGHT, startX, board.getSquareSize() - 1, index);
    }

    @Override
    public Sudoku getSudoku() {
        return new Sudoku(
                mGrid.getCells(),
                mGrid.getGrid(),
                mGrid.getBoards(),
                mGrid.MAX_LIMIT_X,
                mGrid.MAX_LIMIT_Y
        );
    }
}
