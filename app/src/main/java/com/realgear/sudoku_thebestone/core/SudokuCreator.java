package com.realgear.sudoku_thebestone.core;

import android.content.Context;

import com.realgear.sudoku_thebestone.data.sudoku.SudokuData;
import com.realgear.sudoku_thebestone.utils.Iteration;
import com.realgear.sudoku_thebestone.utils.Level;

import java.util.List;
import java.util.ListIterator;
import java.util.Random;

public class SudokuCreator {

    public static boolean create(Sudoku sudoku, SudokuData sudokuData, Level level, SudokuTypes type) {
        //Save Board Before Continuing
        genBoard(sudoku, type);
        sudoku.initIndex();

        //Save Solved Board
        sudokuData.getBoardData().setSolvedBoard(sudoku.getGrids());

        sudoku.saveSolution();

        int limit = sudoku.getBoards().get(0).getSquareSize();
        limit = limit * limit;

        randomizeBlankCellPositions(sudoku, level, limit);

        generateBlankCells(sudoku, level.getBlankCellsNumber(new Random(), limit), level.getIterationType());

        //Save Unsolved Board
        sudokuData.getBoardData().setUnsolvedBoard(sudoku.getGrids());

        for(Cell c : sudoku.getCells()) {
            if(c.isActive()) {
                c.setFixed();
            }
        }

        sudokuData.getBoardData().setCurrentBoard(sudoku.getGrids());

        sudokuData.Save();

        sudokuData.getBoardData().setCreatedDate();

        //Log.e("Gameplay", "Sudoku Creator Is Done");
        return true;
    }

    public static boolean load(Sudoku sudoku, SudokuData sudokuData) {
        int[][] solvedBoard = sudokuData.getBoardData().getSolvedBoard();
        int[][] unsolvedBoard = sudokuData.getBoardData().getUnsolvedBoard();
        int[][] currentBoard = sudokuData.getBoardData().getCurrentBoard();

        for(int x = 0; x < sudoku.getMaxLimitX(); x++) {
            for (int y = 0; y < sudoku.getMaxLimitY(); y++) {
                Cell cell = sudoku.getCell(x, y);

                int solvedValue = solvedBoard[x][y];

                if(cell.isActive()) {
                    cell.setValue(solvedValue);
                }
            }
        }

        sudoku.initIndex();


        for(int x = 0; x < sudoku.getMaxLimitX(); x++) {
            for(int y = 0; y < sudoku.getMaxLimitY(); y++) {
                //int solvedValue = solvedBoard[x][y];
                int unsolvedValue = unsolvedBoard[x][y];
                int currentValue = currentBoard[x][y];

                Cell cell = sudoku.getCell(x, y);

                if(cell.isActive()) {
                    //Set Value
                    //cell.setValue(solvedValue);

                    //Save Solution
                    cell.setSolution();

                    //Set Unsolved Value
                    cell.setValue(unsolvedValue);

                    //Set Is Fixed
                    cell.setFixed();

                    //Set Our Current Modified
                    if(!cell.isFixed()) {
                        cell.setValue(currentValue);
                    }
                }
            }
        }

        //sudoku.initIndex();

        return true;
    }

    private static void genBoard(Sudoku sudoku, SudokuTypes types) {

        switch (types) {
            case STANDARD:
                SudokuSolver solver = new SudokuSolver();
                solver.setSudoku(sudoku);
                solver.solve(1);
                break;
            case SAMURAI:
                SamuraiSudokuSolver solver1 = new SamuraiSudokuSolver();
                solver1.setSudoku(sudoku);
                solver1.solve(1);
                break;
        }
    }

    private static void randomizeBlankCellPositions(Sudoku sudoku, Level level, int size) {
        if(level.equals(Level.HARD) || level.equals(Level.INSANE)) {
            generateBlankCells(sudoku, level.getBlankCellsNumber(new Random(), size), Iteration.RANDOM);
        }
    }

    private static void generateBlankCells(Sudoku sudoku, int limit, Iteration iteration) {
        sudoku.setIterationType(iteration);
        List<Board> boards = sudoku.getBoards();
        for (Board board : boards) {
            ListIterator<Cell> iterator = (ListIterator<Cell>)board.iterator();

            while (iterator.hasNext()) {
                Cell current = iterator.next();

                if(!current.isBlank()) {
                    current.setValue(0);
                }
                else {
                    continue;
                }

                if(isOutOfLimits(limit, board.getBlankCells().size())) {
                    break;
                }
            }

            board.setIterationType(Iteration.LINEAR);
        }
    }

    public static boolean isOutOfLimits(int limit, int size) {
        return size >= limit;
    }

    public static void loadFromFile(Sudoku sudoku, SudokuData sudokuData, Level level, Context context) {
        int[][] grid = new LoadSudokuGrid(context).getPuzzlesArray().get(0);

        for(int x = 0; x < sudoku.getMaxLimitX(); x++) {
            for(int y = 0; y < sudoku.getMaxLimitY(); y++) {
                sudoku.getCell(x, y).setValue(grid[x][y]);
            }
        }

        sudoku.initIndex();

        //Save Solved Board
        sudokuData.getBoardData().setSolvedBoard(sudoku.getGrids());

        sudoku.saveSolution();

        int limit = sudoku.getBoards().get(0).getSquareSize();
        limit = limit * limit;

        randomizeBlankCellPositions(sudoku, level, limit);

        generateBlankCells(sudoku, level.getBlankCellsNumber(new Random(), limit), level.getIterationType());

        //Save Unsolved Board
        sudokuData.getBoardData().setUnsolvedBoard(sudoku.getGrids());

        for(Cell c : sudoku.getCells()) {
            if(c.isActive()) {
                c.setFixed();
            }
        }

        sudokuData.getBoardData().setCurrentBoard(sudoku.getGrids());

        sudokuData.Save();
    }
}
