package com.realgear.sudoku_thebestone.data;

import android.content.Context;
import android.util.Log;

import com.realgear.sudoku_thebestone.core.GameType;
import com.realgear.sudoku_thebestone.core.Level;
import com.realgear.sudoku_thebestone.core.SudokuTypes;
import com.realgear.sudoku_thebestone.utils.SamuraiGridType;

import java.util.ArrayList;
import java.util.List;

public class LevelData extends DataPref {
    public LevelData(Context context) {
        super(context, LevelData.class.getSimpleName());
    }

    public void saveLevel(GameType gameType,
                          SudokuTypes sudokuType,
                          SamuraiGridType gridType,
                          Level level) {

        List<Level> levels = getLevel(gameType, sudokuType, gridType);

        levels.add(level);
        String key = getKey(gameType, sudokuType, gridType);
        putObject(key, levels);

        Log.e("LevelData", "Save Level Complete");
        Log.e("LevelData", "Key = {"+key+"}");
    }

    public List<Level> getLevel(GameType gameType, SudokuTypes sudokuType, SamuraiGridType gridType)
    {
        String key = getKey(gameType, sudokuType, gridType);
        Object levels = getObject(key);

        if(gameType == GameType.Beginner &&
                sudokuType == SudokuTypes.SAMURAI &&
        gridType == SamuraiGridType.GRID_2) {
            Log.e("LevelData", "Key = {" + key + "}");
            Log.e("LevelData", "Null = " + (levels == null));
        }

        if(levels == null)
            return new ArrayList<>();

        try {
            return (List<Level>)levels;
        }
        catch (Exception e) {
            if(gameType == GameType.Beginner &&
                    sudokuType == SudokuTypes.SAMURAI &&
                    gridType == SamuraiGridType.GRID_2)
            Log.e("Level Data", e.toString());
            return new ArrayList<>();
        }
    }

    public String getKey(GameType gameType, SudokuTypes sudokuType, SamuraiGridType gridType)
    {
        return sudokuType.name() + "_" + gameType.name() + ((sudokuType == SudokuTypes.SAMURAI) ? "_" + gridType : "");
    }
}
