package com.realgear.sudoku_thebestone.core;

import com.realgear.sudoku_thebestone.utils.Iteration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

public class Sudoku {
    private final List<Cell>    mCells;
    private final Cell[][]      mGrids;
    private final List<Board>   mBoards;
    //private final int mGridSquareSize;
    private final int mSquareSize;
    private final int mSize;

    private final int MAX_LIMIT_X;
    private final int MAX_LIMIT_Y;

    private List<Hints> mHints;
    private TreeMap<Integer,Integer> mIndexCount;

    public Sudoku(List<Cell> cells, Cell[][] grids, List<Board> boards, int maxLimitX, int maxLimitY) {
        this.mCells = cells;
        this.mGrids = grids;

        this.mBoards = boards;
        this.MAX_LIMIT_X = maxLimitX;
        this.MAX_LIMIT_Y = maxLimitY;

        this.mSquareSize = boards.get(0).getSquareSize();
        this.mSize = boards.get(0).getSize();
        this.mHints = new ArrayList<>();
    }

    public void initHints() {
        for(Cell c: mCells) {

            List<Integer> hints = new ArrayList<>();
            for(int i = 0; i < mSquareSize; i++) {
                int index = i + 1;
                List<Board> boards = getLinkedBoards(c);
                boolean isValid = true;
                for(Board board : boards) {
                    if(!board.testConditions(c, index)) {
                        isValid = false;
                    }
                }

                if(isValid) {
                    hints.add(index);
                }
            }

            c.setHint(new Hints(hints));
        }

        /*
        List<Hints> hints = new ArrayList<>();
        for(Board b : mBoards)
        {
            List<Cell> blanks = b.getBlankCells();
            for (Cell cell : blanks) {
                List<Integer> temp = new ArrayList<>();
                for(int i = 0; i < mSquareSize; i++) {
                    int index = i + 1;
                    if(b.testConditions(cell, index)) {
                        temp.add(index);
                    }
                }
                if(temp.size() > 0) {
                    hints.add(new Hints(cell.getX(), cell.getY(), temp));
                }
            }
        }

        List<Hints> result = new ArrayList<>();
        if(hints.size() > 0) {
            for(Hints h : hints) {
                Hints n = null;
                for(Hints x : hints) {
                    if(x == h) {
                        continue;
                    }

                    if(x.getX() == h.getX() && x.getY() == h.getY() && h != x) {

                        List<Integer> temp = new ArrayList<>();
                        List<Integer> vals_1 = h.getHints();
                        List<Integer> vals_2 = x.getHints();

                        for(int i : vals_1) {
                            for(int j : vals_2) {
                                if(i == j) {
                                    temp.add(i);
                                }
                            }
                        }
                        n = new Hints(h.getX(), h.getY(), temp);
                    }
                }
                result.add((n == null) ? h : n);
            }
            mHints = result;
        }
        //return result;*/
    }

    public List<Integer> recheckHints(Cell cell) {
        List<Integer> hints = new ArrayList<>();
        for(int i = 0; i < mSquareSize; i++) {
            int index = i + 1;
            List<Board> boards = getLinkedBoards(cell);
            boolean isValid = true;
            for(Board board : boards) {
                if(!board.testConditions(cell, index)) {
                    isValid = false;
                }
            }

            if(isValid) {
                hints.add(index);
            }
        }
        return hints;
    }

    public void initIndex() {
        mIndexCount = new TreeMap<>();
        for(int i = 0; i < mSquareSize; i++) {
            int index = i + 1;
            int count = 0;
            for(Cell cell : mCells) {
                if(cell.isActive()) {
                    if(cell.getValue() == index) {
                        count++;
                    }
                }
            }
            mIndexCount.put(index, count);
        }
    }

    public List<Board> getBoards() {
        return this.mBoards;
    }

    public Cell[][] getGrid() {
        return this.mGrids;
    }

    public int[][] getGrids() {
        int[][] result = new int[MAX_LIMIT_X][MAX_LIMIT_Y];
        //Log.e("Cell_Test", "Header");
        for(int x = 0; x < MAX_LIMIT_X; x++) {
            for(int y = 0; y < MAX_LIMIT_Y; y++) {
                result[x][y] = mGrids[x][y].getValue();
                //Log.e("Cell_Test", "Cell {"+x+","+y+"} = " + mGrids[x][y].getValue());
            }
        }

        return result;
    }


    public int getMaxLimitX() {
        return this.MAX_LIMIT_X;
    }

    public int getMaxLimitY() {
        return this.MAX_LIMIT_Y;
    }

    public int getBoardSquareSize() {
        return this.mSquareSize;
    }

    public void saveSolution() {
        for(Cell c : mCells) {
            if(c == null)
                continue;

            c.setSolution();
        }
    }

    public void setIterationType(Iteration iteration) {
        for (Board board : mBoards) {
            board.setIterationType(iteration);
        }
    }

    public List<Cell> getCells() {
        return this.mCells;
    }

    public Cell getCell(int x, int y) {
        return this.mGrids[x][y];
    }

    public int getLeftCount(int index) {
        int count = mIndexCount.get(index);
        for(Cell c : mCells) {
            if(c.isActive()) {
                if(c.getValue() == index) {
                    count--;
                }
            }
        }

        return count;
    }

    public boolean checkBoard() {

        for (Board board : mBoards) {
            if(!board.checkBoard()) {
                return false;
            }
        }

        return true;
    }

    public HashMap<Cell, CellState.Status> validateBoard() {
        HashMap<Cell, CellState.Status> result = new HashMap<>();

        for(Cell c : mCells) {
            if(c == null || c.isFixed() || c.isBlank())
                continue;

            List<Board> boards = getLinkedBoards(c);
            boolean isValid = true;

            for(Board board : boards) {
                if(!board.validateCell(c, false)) {
                    isValid = false;
                }
            }

            if(c.getValue() == c.getSolution()) {
                result.put(c, CellState.Status.SOLVED);
            }
            else if(!isValid) {
                result.put(c, CellState.Status.ERROR);
            }
        }

        for(Cell c : result.keySet()) {
            //Log.e("Sudoku", "Cell Status {"+c.getX() + "," + c.getY() +"} -> " + result.get(c));
        }

        return result;
    }

    public List<Board> getLinkedBoards(Cell cell) {
        List<Board> boards = new ArrayList<>();

        for(Board b: mBoards) {
            if(b.containsCell(cell)) {
                boards.add(b);
            }
        }

        return boards;
    }

    public List<Cell> getBlankCells() {
        List<Cell> blanks = new ArrayList<>();

        for(Cell cell : mCells) {
            if(cell.getValue() == 0) {
                blanks.add(cell);
            }
        }

        return blanks;
    }

    public int getSize() {
        return this.mSize;
    }
}
