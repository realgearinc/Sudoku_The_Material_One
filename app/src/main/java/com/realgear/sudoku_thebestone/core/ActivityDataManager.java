package com.realgear.sudoku_thebestone.core;

import com.realgear.sudoku_thebestone.MainActivity;
import com.realgear.sudoku_thebestone.data.sudoku.SudokuData;
import com.realgear.sudoku_thebestone.utils.SamuraiGridType;

import java.util.TreeMap;

public class ActivityDataManager {
    private final String TAG = ActivityDataManager.class.getSimpleName();

    private TreeMap<SudokuTypes, TreeMap<GameType, SudokuData>> mSudokuData;

    private TreeMap<SamuraiGridType, TreeMap<GameType, SudokuData>> mSamuraiData;


    private MainActivity mActivity;

    public ActivityDataManager(MainActivity activity) {
        this.mActivity = activity;
        this.mSudokuData = getSudokuData();
        this.mSamuraiData = getSamuraiData();
    }

    private TreeMap<SudokuTypes, TreeMap<GameType, SudokuData>> getSudokuData() {
        TreeMap<SudokuTypes, TreeMap<GameType, SudokuData>> result = new TreeMap<>();

        for(SudokuTypes type: SudokuTypes.values()) {
            if(type == SudokuTypes.SAMURAI)
                continue;

            TreeMap<GameType, SudokuData> temp = new TreeMap<>();
            for(GameType game: GameType.values()) {
                temp.put(game, new SudokuData(mActivity.getApplicationContext(), null, game, type));
            }

            result.put(type, temp);
        }

        return result;
    }

    private TreeMap<SamuraiGridType, TreeMap<GameType, SudokuData>> getSamuraiData() {
        TreeMap<SamuraiGridType, TreeMap<GameType, SudokuData>> result = new TreeMap<>();
        for(SamuraiGridType gridType : SamuraiGridType.values()) {

            TreeMap<GameType, SudokuData> temp = new TreeMap<>();
            for(GameType gameType : GameType.values()) {
                temp.put(gameType, new SudokuData(mActivity.getApplicationContext(),  gridType, gameType, SudokuTypes.SAMURAI));
            }

            result.put(gridType, temp);
        }

        return result;
    }

    public SudokuData getSudokuData(SamuraiGridType gridType, GameType level, SudokuTypes type) {
        if(type == SudokuTypes.SAMURAI) {
            if(!mSamuraiData.containsKey(gridType))
                return null;

            if(!mSamuraiData.get(gridType).containsKey(level))
                return null;

            return mSamuraiData.get(gridType).get(level);
        }
        else {
            if (!mSudokuData.containsKey(type))
                return null;

            if (!mSudokuData.get(type).containsKey(level))
                return null;

            return mSudokuData.get(type).get(level);
        }
    }
}
