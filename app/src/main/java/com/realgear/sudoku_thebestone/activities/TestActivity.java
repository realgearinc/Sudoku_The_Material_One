package com.realgear.sudoku_thebestone.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.realgear.sudoku_thebestone.R;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        /*GridLayout gridLayout = (GridLayout) findViewById(R.id.sudokuBoard_GridLayout);
        //SamuraiSudoku samuraiSudoku = new SamuraiSudoku();

        Sudoku sudoku = new StandardSudoku(3).getSudoku();
        SudokuSolver solver = new SudokuSolver().setSudoku(sudoku);
        solver.solve(1);

        SudokuCreator.create(sudoku, mSudokuData, Level.EASY);

        for(Board b : sudoku.getBoards()) {
            Log.e("TESTACTIVITY", b.toString());
        }

        int mSquareSize = sudoku.getSquareSize();
        Cell[][] grid = sudoku.getGrid();

        gridLayout.setColumnCount(mSquareSize);
        gridLayout.setRowCount(mSquareSize);
        gridLayout.setUseDefaultMargins(false);

        for(int x = 0; x < mSquareSize; x++) {
            for(int y = 0; y < mSquareSize; y++) {
                Button cell = new Button(this);
                cell.setVisibility(grid[x][y].isActive() ? View.VISIBLE : View.INVISIBLE);
                cell.setText(String.valueOf(grid[x][y].getValue()));
                gridLayout.addView(cell, 60, 60);
            }
        }*/
    }
}