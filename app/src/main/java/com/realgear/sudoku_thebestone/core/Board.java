package com.realgear.sudoku_thebestone.core;

import android.util.Log;

import com.realgear.sudoku_thebestone.utils.Iteration;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class Board implements Iterable<Cell>, Cloneable {
    private static String TAG = Board.class.getSimpleName();

    public void setCellsBlank() {
        for(Cell cell: mCells) {
            if(cell.isActive() && !cell.isExtra()) {
                cell.setValue(0);
            }
        }
    }



    public enum MoveType {
        RIGHT,
        LEFT,
        DOWN,
        UP
    }

    final private Cell[][] mGrid;
    final private List<CellBlock> mBlocks;
    final private List<Cell> mCells;
    final private List<Cell> mThisCells;
    final private int mSquareSize;
    final private int mSize;
    final private int mOffsetX;
    final private int mOffsetY;

    final private CheckCondition mCheckCondition;

    private Iteration iteration = Iteration.LINEAR;

    public Board(Cell[][] grid, List<Cell> cells, int size, int squareSize, int offsetX, int offsetY) {
        this.mGrid = grid;
        this.mCells = cells;
        this.mThisCells = new ArrayList<>(squareSize * squareSize);
        this.mSize = size;
        this.mSquareSize = squareSize;

        this.mOffsetX = offsetX;
        this.mOffsetY = offsetY;

        this.mBlocks = createBlocks(mSquareSize);

        for (int x = mOffsetX; x < (mOffsetX + mSquareSize); x++) {
            for (int y = mOffsetY; y < (mOffsetY + mSquareSize); y++) {
                int nX = x - mOffsetX;
                int nY = y - mOffsetY;

                int s_x = nX / mSize;
                int s_y = nY / mSize;

                int block = (int)(s_x + (s_y * mSize));
                mGrid[x][y].setBlock(block);
                mBlocks.get(block).add(mGrid[x][y]);
                mThisCells.add(mGrid[x][y]);
            }
        }

        //Log.e("OFFSET", "Offset Y = " + mOffsetY);

        mCheckCondition = new CheckCondition(this, grid, cells, mBlocks, squareSize, offsetX, offsetY);

        setCellsRules();
    }

    private void setCellsRules() {
        for(CellBlock block : mBlocks) {
            for(Cell cell : block.getCells()) {

                cell.setCellRule(CellRule.ANYWHERE_CENTER);

                if(mSquareSize == 125) {
                    if (block.cleanCompareY(cell, 0)) {
                        if (block.cleanCompareX(cell, 2)) {
                            cell.setCellRule(CellRule.LOWER_2X0_L);
                        } else
                            cell.setCellRule(CellRule.CENTER_2X0);
                    }

                    if (block.cleanCompareX(cell, 0)) {
                        if (block.cleanCompareY(cell, 2)) {
                            cell.setCellRule(CellRule.UPPER_0X2_L);
                        } else
                            cell.setCellRule(CellRule.UPPER_0X2);
                    }

                    if (block.cleanCompareY(cell, 2)) {
                        if (!block.cleanCompareX(cell, 0)) {
                            cell.setCellRule(CellRule.CENTER_RIGHT_END);
                        }
                    }

                    if (block.cleanCompareX(cell, 2)) {
                        if (!block.cleanCompareY(cell, 0))
                            cell.setCellRule(CellRule.CENTER_LOWER_END);
                    }

                    if (block.cleanCompare(cell, 2, 2)) {
                        cell.setCellRule(CellRule.LOWER_RIGHT_END_CORNER);
                    }

                    if (block.cleanCompare(cell, 0, 0)) {
                        cell.setCellRule(CellRule.UPPER_CORNER_0);
                    }

                    if (cleanCompareX(cell, mSquareSize - 1)) {

                        if (block.cleanCompare(cell, 2, 0)) {
                            cell.setCellRule(CellRule.LOWER_LEFT_0);
                        } else if (block.cleanCompare(cell, 2, 2)) {
                            cell.setCellRule(CellRule.LOWER_3X2);
                        } else {
                            cell.setCellRule(CellRule.CENTER_3X2_L);
                        }
                    }

                    if (cleanCompareY(cell, mSquareSize - 1)) {
                        if (block.cleanCompareY(cell, 2)) {
                            cell.setCellRule(CellRule.CENTER_2X3_L);
                        }

                        if (block.cleanCompare(cell, 0, 2)) {
                            cell.setCellRule(CellRule.UPPER_0X3);
                        }
                    }

                    if (cleanCompareY(cell, mSquareSize - 1)) {
                        if (block.cleanCompare(cell, 2, 2)) {
                            if (mGrid.length == cell.getX() + 1 || mGrid.length == cell.getY() + 1 && cleanCompareY(cell, mSquareSize - 1) || cleanCompareX(cell, mSquareSize - 1)) {
                                if (cleanCompare(cell, mSquareSize - 1, mSquareSize - 1))
                                    cell.setCellRule(CellRule.LOWER_3X3_L);
                            }
                        }
                    }
                }

                int size = mSize - 1;

                if (block.cleanCompareY(cell, 0)) {
                    if (block.cleanCompareX(cell, size)) {
                        cell.setCellRule(CellRule.LOWER_2X0_L);
                    } else
                        cell.setCellRule(CellRule.CENTER_2X0);
                }

                if (block.cleanCompareX(cell, 0)) {
                    if (block.cleanCompareY(cell, size)) {
                        cell.setCellRule(CellRule.UPPER_0X2_L);
                    } else
                        cell.setCellRule(CellRule.UPPER_0X2);
                }

                if (block.cleanCompareY(cell, size)) {
                    if (!block.cleanCompareX(cell, 0)) {
                        cell.setCellRule(CellRule.CENTER_RIGHT_END);
                    }
                }

                if (block.cleanCompareX(cell, size)) {
                    if (!block.cleanCompareY(cell, 0))
                        cell.setCellRule(CellRule.CENTER_LOWER_END);
                }

                if (block.cleanCompare(cell, size, size)) {
                    cell.setCellRule(CellRule.LOWER_RIGHT_END_CORNER);
                }

                if (block.cleanCompare(cell, 0, 0)) {
                    cell.setCellRule(CellRule.UPPER_CORNER_0);
                }

                if (cleanCompareX(cell, mSquareSize - 1)) {

                    if (block.cleanCompare(cell, size, 0)) {
                        cell.setCellRule(CellRule.LOWER_LEFT_0);
                    } else if (block.cleanCompare(cell, size, size)) {
                        cell.setCellRule(CellRule.LOWER_3X2);
                    } else {
                        cell.setCellRule(CellRule.CENTER_3X2_L);
                    }
                }

                if (cleanCompareY(cell, mSquareSize - 1)) {
                    if (block.cleanCompareY(cell, size)) {
                        cell.setCellRule(CellRule.CENTER_2X3_L);
                    }

                    if (block.cleanCompare(cell, 0, size)) {
                        cell.setCellRule(CellRule.UPPER_0X3);
                    }
                }

                if (cleanCompareY(cell, mSquareSize - 1)) {
                    if (block.cleanCompare(cell, size, size)) {
                        if (mGrid.length == cell.getX() + 1 || mGrid.length == cell.getY() + 1 && cleanCompareY(cell, mSquareSize - 1) || cleanCompareX(cell, mSquareSize - 1)) {
                            if (cleanCompare(cell, mSquareSize - 1, mSquareSize - 1))
                                cell.setCellRule(CellRule.LOWER_3X3_L);
                        }
                    }
                }

                //Do CleanUp

            }
        }
    }

    public void MoveLine(MoveType moveType, int index) {
        switch (moveType) {
            case LEFT:
            case RIGHT:
                moveHorizontal(moveType, index);
                break;
            case UP:
            case DOWN:
                moveVertical(moveType, index);

        }
    }

    private void moveHorizontal(MoveType moveType, int index) {
        List<Cell> cells = new ArrayList<>();
        for(int i = 0; i < mSquareSize; i++) {
            int x = mOffsetX + i;
            Cell cell = mGrid[x][mOffsetY + index];
            Cell cell1 = mGrid[x][mOffsetY + (index - 1)];
            if (cell.getCellRule() == CellRule.UPPER_CORNER_0) {
                if (cell1 != null && cell1.isActive()) {

                    if (cell1.getCellRule() == CellRule.UPPER_0X2_L) {
                        cell1.setCellRule(CellRule.UPPER_0X1);
                    }
                    else if (cell1.getCellRule() == CellRule.CENTER_3X2_L) {
                        cell1.setCellRule(CellRule.LOWER_3X3_L);
                    }
                    else {
                        if(containsCell(cell1)) {
                            int pos = getBlockByPos(cell1.getX(), cell1.getY());
                            if(mBlocks.get(pos).cleanCompare(cell1, 0, 2)) {
                                cell1.setCellRule(CellRule.UPPER_0X3);
                            }
                            else
                                cell1.setCellRule(CellRule.CENTER_2X3_L);
                        }
                        else
                            cell1.setCellRule(CellRule.CENTER_2X3_L);
                    }

                    cell.setCellRule(CellRule.UPPER_0X2);
                }
            }
            else if (cell.getCellRule() == CellRule.CENTER_2X0) {
                if (cell1 != null && cell1.isActive()) {
                    if (cell1.getCellRule() == CellRule.UPPER_0X2) {
                        cell1.setCellRule(CellRule.UPPER_0X1);
                    }
                    else if (cell1.getCellRule() == CellRule.CENTER_3X2_L) {
                        cell1.setCellRule(CellRule.LOWER_3X3_L);
                    }
                    else {
                        cell1.setCellRule(CellRule.CENTER_2X3_L);
                    }

                    cell.setCellRule(CellRule.ANYWHERE_CENTER);
                }
            }
            else if(cell.getCellRule() == CellRule.CENTER_1X0) {
                if (cell1 != null && cell1.isActive()) {
                    if (cell1.getCellRule() == CellRule.UPPER_0X2) {
                        cell1.setCellRule(CellRule.UPPER_0X1);
                    }
                    else if (cell1.getCellRule() == CellRule.CENTER_3X2_L) {
                        cell1.setCellRule(CellRule.LOWER_3X3_L);
                    }
                    else if(cell1.getCellRule() == CellRule.LOWER_3X2 ||
                        cell1.getCellRule() == CellRule.LOWER_RIGHT) {
                        cell1.setCellRule(CellRule.LOWER_3X3_L);
                    }
                    else {
                        cell1.setCellRule(CellRule.CENTER_2X3_L);
                    }

                    cell.setCellRule(CellRule.CENTER_3X2_L);
                }
            }
            else if(cell.getCellRule() == CellRule.LOWER_LEFT_0 ||
            cell.getCellRule() == CellRule.LOWER_2X0_L) {
                if (cell1 != null && cell1.isActive()) {
                    if (cell1.getCellRule() == CellRule.UPPER_0X2) {
                        cell1.setCellRule(CellRule.UPPER_0X1);
                    }
                    else if (cell1.getCellRule() == CellRule.CENTER_3X2_L) {
                        cell1.setCellRule(CellRule.LOWER_3X3_L);
                    }
                    else if(cell1.getCellRule() == CellRule.LOWER_3X2 ||
                            cell1.getCellRule() == CellRule.LOWER_RIGHT) {
                        cell1.setCellRule(CellRule.LOWER_3X3_L);
                    }
                    else if(cell1.getCellRule() == CellRule.LOWER_RIGHT_END_CORNER) {
                        cell1.setCellRule(CellRule.CENTER_2X3);
                    }
                    else {
                        cell1.setCellRule(CellRule.CENTER_2X3_L);
                    }

                    if(cell.getCellRule() == CellRule.LOWER_2X0_L) {
                        cell.setCellRule(CellRule.CENTER_LOWER_END);
                    }
                    else {
                        cell.setCellRule(CellRule.CENTER_3X2_L);
                    }
                }
            }
        }
    }

    private void moveVertical(MoveType moveType, int index) {
        Cell[] cells = mGrid[mOffsetX + index];
        if(moveType == MoveType.UP) {
            for(int y = mOffsetY; y < (mOffsetY + mSquareSize); y++) {
                Cell cell = cells[y];
                if(cell.getCellRule() == CellRule.UPPER_CORNER_0) {
                    Cell cell1 = mGrid[cell.getX() - 1][cell.getY()];
                    if(cell1 != null && cell1.isActive()) {
                        if(cell1.getCellRule() == CellRule.LOWER_2X0_L) {
                            cell1.setCellRule(CellRule.CENTER_1X0);
                        }
                        else if(cell1.getCellRule() == CellRule.LOWER_LEFT_0) {

                        }
                        else {
                            cell1.setCellRule(CellRule.CENTER_3X2_L);
                        }
                        cell.setCellRule(CellRule.CENTER_2X0);
                    }
                }
                else if(cell.getCellRule() == CellRule.UPPER_0X2 ||
                        cell.getCellRule() == CellRule.UPPER_0X2_L) {
                    Cell cell1 = mGrid[cell.getX() - 1][cell.getY()];
                    if(cell1 != null && cell1.isActive()) {
                        cell1.setCellRule(CellRule.CENTER_3X2_L);
                        CellBlock block = mBlocks.get(getBlockByPos(cell.getX(), cell.getY()));
                        if(block != null) {
                            if (block.cleanCompare(cell, 0, 2)) {
                                cell1.setCellRule(CellRule.LOWER_3X2);
                                cell.setCellRule(CellRule.CENTER_RIGHT_END);
                            } else {
                                cell.setCellRule(CellRule.ANYWHERE_CENTER);
                            }
                        }
                        else
                            cell.setCellRule(CellRule.ANYWHERE_CENTER);
                    }
                }
                else if(cell.getCellRule() == CellRule.UPPER_0X3) {
                    Cell cell1 = mGrid[cell.getX() - 1][cell.getY()];
                    if(cell1 != null && cell1.isActive()) {

                        if(cell1.getCellRule() != CellRule.LOWER_3X2) {
                            cell1.setCellRule(CellRule.LOWER_3X3_L);
                        }

                        CellBlock block = mBlocks.get(getBlockByPos(cell.getX(), cell.getY()));
                        if(block != null) {
                            cell.setCellRule(CellRule.CENTER_2X3_L);
                        }
                        else
                            cell.setCellRule(CellRule.ANYWHERE_CENTER);
                    }
                }
            }
        }
    }

    public void addExtraVertical(MoveType moveType, int startX, int startY, int index) {
        for(int i = 0; i < index; i++) {
            Cell curCell = mGrid[mOffsetX + (startX + i)][mOffsetY + startY];
            if(curCell != null) {
                int eq = -1;
                switch (moveType) {
                    case RIGHT: eq = 1;
                }
                Cell extra = mGrid[curCell.getX()][curCell.getY() + eq];

                //Log.e(TAG, "Extra Cell : {"+extra.getX()+"," + extra.getY() + "}");

                if(!extra.isActive()) {
                    extra.setIsExtra(true);
                    if(moveType == MoveType.LEFT)
                        extra.setCellRule(CellRule.EXTRA_0X0);
                    else
                        extra.setCellRule(CellRule.EXTRA_1X0);
                }

                if(curCell.getCellRule() == CellRule.CENTER_2X0) {
                    curCell.setCellRule(CellRule.ANYWHERE_CENTER);
                }
                else if(curCell.getCellRule() == CellRule.LOWER_LEFT_0) {
                    curCell.setCellRule(CellRule.CENTER_3X2_L);
                }

                if(curCell.getCellRule() == CellRule.CENTER_2X3_L) {
                    curCell.setCellRule(CellRule.ANYWHERE_CENTER);
                }
                else if(curCell.getCellRule() == CellRule.LOWER_3X3_L) {
                    curCell.setCellRule(CellRule.LOWER_3X2);
                }
            }
        }
    }

    public boolean cleanCompare(Cell c, int x, int y) {
        int cX = (c.getX() - mOffsetX);
        int cY = (c.getY() - mOffsetY);

        //Log.e("CLEAN", "Position : {" +cX + "x" + cY+"}" );

        if(cX == x && cY == y) {
            //Log.e("CLEAN", "Position : {" +cX + "x" + cY+"}" );
            return true;
        }

        return false;
    }

    public boolean cleanCompareX(Cell c, int x) {
        int cX = (c.getX() - mOffsetX);

        return cX == x;
    }

    public boolean cleanCompareY(Cell c, int y) {
        int cY = (c.getY() - mOffsetY);

        return cY == y;
    }

    public boolean containsCell(Cell c) {
        return mThisCells.contains(c);
    }

    public Cell getCell(int x, int y, boolean isClean) {
        try {
            if (!isClean) {
                Cell result = mGrid[x][y];
                return result;
            } else {
                int cX = (x + mOffsetX);
                int cY = (y + mOffsetY);

                Cell result = mGrid[cX][cY];
                return result;
            }
        }
        catch (Exception e) {
            return null;
        }
    }

    public boolean testConditions(Cell cell, int value) {
        return testBlock(cell, value) && testColumn(cell.getY(), value) && testRow(cell.getX(), value);
    }

    public boolean testRow(int row, int value) {
        for(int y = mOffsetY; y < (mOffsetY + mSquareSize); y++) {
            if(mGrid[row][y].getValue() == value) {
                return false;
            }
        }
        return true;
    }

    public boolean testColumn(int column, int value) {
        for(int x = mOffsetX; x < (mOffsetX + mSquareSize); x++) {
            if(mGrid[x][column].getValue() == value) {
                return false;
            }
        }
        return true;
    }

    public boolean testBlock(Cell testedCell, int value) {
        int block =  getBlockByPos(testedCell.getX(), testedCell.getY());
        boolean result  = mBlocks.get(block).hasValue(value);

        if(testedCell.getX() == 3 && testedCell.getY() == 3)
        if(result) {
            //Log.e("Board","Cell {" + testedCell.getX() + "," + testedCell.getY() + "}");
            //Log.e("Board", "Block {" + block + "} Contains Value : " + value);
        }

        return !result;
    }

    public List<CellBlock> createBlocks(int capacity) {
        List<CellBlock> list = new ArrayList<>();

        for(int i = 0; i < capacity; i++) {
            list.add(new CellBlock(mSquareSize));
        }

        return list;
    }

    public int getSquareSize() {
        return this.mSquareSize;
    }

    public int getSize() {
        return this.mSize;
    }

    public int getBlockByPos(int x, int y){
        int nX = x - mOffsetX;
        int nY = y - mOffsetY;

        int s_x = nX / mSize;
        int s_y = nY / mSize;

        int block = (int)(s_x + (s_y * mSize));

        return block;
    }

    public List<Cell> getBlankCells() {
        List<Cell> blanks = new ArrayList<>();

        for(Cell cell : this) {
            if(cell.isBlank()) {
                blanks.add(cell);
            }
        }

        return blanks;
    }

    //Iterators - Important don't touch this code
    @NotNull
    @Override
    public Iterator<Cell> iterator() {
        switch (iteration) {
            case RANDOM: return randomOrderIterator();

            default: return linearOrderIterator();
        }
    }

    private ListIterator<Cell> randomOrderIterator() {
        ArrayList<Cell> randomOrder = new ArrayList<Cell>(mThisCells);
        Collections.shuffle(randomOrder);
        return randomOrder.listIterator();
    }

    private ListIterator<Cell> linearOrderIterator()
    {
        return mThisCells.listIterator();
    }

    @Override
    public String toString() {
        String result = "";

        result += "Boards { " + mSquareSize + "x" + mSquareSize +" } \n";
        result += "Cells { " + mSquareSize * mSquareSize + " } \n";


        for(int x = mOffsetX; x < (mOffsetX + mSquareSize); x++) {
            for(int y = mOffsetY; y < (mOffsetY + mSquareSize); y++) {
                try {
                    if (mGrid[x][y] != null)
                        result += mGrid[x][y].getValue() + ",";
                    else
                        Log.e(TAG, "Grid {" + x + "x" + y + "} == NULL");
                }
                catch (Exception e) {
                    Log.e(TAG, "Offset : {" + mOffsetX + "x" + mOffsetY +"}");
                    Log.e(TAG, "Index : {" + x + "x" + y + "}");
                }
            }
            result += "\n";
        }

        return result;
    }

    public void setIterationType(Iteration iteration) {
        this.iteration = iteration;
    }

    public boolean checkBoard() {
        for(Cell c: this) {
            if(c != null) {
                if (!c.isFixed()) {
                    if (!c.isBlank()) {
                        if (mCheckCondition.check(c).size() != 0) {
                            return false;
                        }
                    } else {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public boolean validateCell(Cell cell, boolean addFixed) {
        List<Cell> result = mCheckCondition.check(cell);
        return (result.size() == 0);
    }
}
