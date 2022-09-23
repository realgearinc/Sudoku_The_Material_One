package com.realgear.sudoku_thebestone.data.sudoku;

import android.content.Context;

import com.realgear.sudoku_thebestone.data.DataPref;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BoardData extends DataPref {
    private final String KEY_SudokuType     = "SUDOKU_TYPE";
    private final String KEY_SamuraiType    = "SAMURAI_TYPE";
    private final String KEY_SudokuSize     = "SUDOKU_SIZE";
    private final String KEY_SolvedBoard    = "SOLVED_BOARD";
    private final String KEY_UnsolvedBoard  = "UNSOLVED_BOARD";
    private final String KEY_CurrentBoard   = "CURRENT_BOARD";
    private final String KEY_CurrentLevel   = "CURRENT_LEVEL";
    private final String KEY_IsSaved        = "IS_SAVED";
    private final String KEY_Time           = "TIME";
    private final String KEY_CreatedDate    = "CREATED_DATE";
    private final String KEY_HintsUsed      = "HINTS_USED";
    private final String KEY_ChecksUsed     = "CHECKS_USED";
    private final String Key_IsCompleted    = "IS_COMPLETED";

    public BoardData(Context context, String key) {
        super(context, (BoardData.class.getSimpleName() + "_" + key));
    }

    public void setSolvedBoard(int[][] solvedBoard) {
        putObject(KEY_SolvedBoard, solvedBoard);
    }
    public void setUnsolvedBoard(int[][] unsolvedBoard) {
        putObject(KEY_UnsolvedBoard, unsolvedBoard);
    }
    public void setCurrentBoard(int[][] currentBoard) {
        putObject(KEY_CurrentBoard, currentBoard);
    }
    public void setSudokuType(int type) {
        putInt(KEY_SudokuType, type);
    }
    public void setSamuraiType(int type) {
        putInt(KEY_SamuraiType, type);
    }
    public void setSudokuSize(int size) {
        putInt(KEY_SudokuSize, size);
    }
    public void setCurrentLevel(int level) {
        putInt(KEY_CurrentLevel, level);
    }
    public void setTime(int time) {
        putInt(KEY_Time, time);
    }
    public void setIsSaved(boolean isSaved) {
        putBoolean(KEY_IsSaved, isSaved);
    }
    public void setCreatedDate() {
        String createdDate = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
        putString(KEY_CreatedDate, createdDate);
    }

    public void onClear() {
        putInt(KEY_ChecksUsed, 0);
        putInt(KEY_HintsUsed, 0);
        putInt(KEY_Time, 0);
        putBoolean(Key_IsCompleted, false);
    }

    public void setCompleted() {
        putBoolean(Key_IsCompleted, true);
    }

    public void setHintsUsed() {
        int used = getHintsUsed();
        if(used <= 0) {
            used = 1;
        }
        else {
            used++;
        }

        putInt(KEY_HintsUsed, used);
    }
    public void setChecksUsed() {
        int used = getChecksUsed();
        if(used <= 0) {
            used = 1;
        }
        else {
            used++;
        }

        putInt(KEY_ChecksUsed, used);
    }

    public boolean getIsSaved() {
        return getBoolean(KEY_IsSaved);
    }

    public int[][] getSolvedBoard() {
        try {
            return (int[][])getObject(KEY_SolvedBoard);
        }
        catch (Exception e) {
            return null;
        }
    }

    public int[][] getUnsolvedBoard() {
        try {
            return (int[][])getObject(KEY_UnsolvedBoard);
        }
        catch (Exception e) {
            return null;
        }
    }

    public int[][] getCurrentBoard() {
        try {
            return (int[][])getObject(KEY_CurrentBoard);
        }
        catch (Exception e) {
            return null;
        }
    }

    public int getSudokuType() {
        return getInt(KEY_SudokuType);
    }

    public int getSamuraiType() {
        return getInt(KEY_SamuraiType);
    }

    public int getSudokuSize() {
        return getInt(KEY_SudokuSize);
    }

    public int getCurrentLevel() {
        return getInt(KEY_CurrentLevel);
    }

    public int getTime() {
        return getInt(KEY_Time);
    }

    public int getHintsUsed() {
        int result = getInt(KEY_HintsUsed);
        return (result >= 0) ? result : 0;
    }
    public int getChecksUsed() {
        int result = getInt(KEY_ChecksUsed);
        return (result >= 0) ? result : 0;
    }

    public boolean isCompleted() {
        return getBoolean(Key_IsCompleted);
    }

    public String getCreatedDate() {
        return getString(KEY_CreatedDate);
    }
}
