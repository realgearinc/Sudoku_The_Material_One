package com.realgear.sudoku_thebestone.activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;


import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.realgear.sudoku_thebestone.R;
import com.realgear.sudoku_thebestone.core.async.AsyncSudokuCreator;
import com.realgear.sudoku_thebestone.core.GameType;
import com.realgear.sudoku_thebestone.core.Sudoku;
import com.realgear.sudoku_thebestone.core.SudokuTypes;
import com.realgear.sudoku_thebestone.core.enums.LevelEnums;
import com.realgear.sudoku_thebestone.core.sudokutypes.SamuraiSudoku;
import com.realgear.sudoku_thebestone.core.sudokutypes.SamuraiSudokuTest;
import com.realgear.sudoku_thebestone.core.sudokutypes.StandardSudoku;
import com.realgear.sudoku_thebestone.data.DailyChallengeData;
import com.realgear.sudoku_thebestone.data.LevelData;
import com.realgear.sudoku_thebestone.data.sudoku.SudokuData;
import com.realgear.sudoku_thebestone.utils.CreatorType;
import com.realgear.sudoku_thebestone.utils.GamePlayType;
import com.realgear.sudoku_thebestone.utils.Keys;
import com.realgear.sudoku_thebestone.utils.Level;
import com.realgear.sudoku_thebestone.utils.SamuraiGridType;
import com.realgear.sudoku_thebestone.view.GameplayView;
import com.realgear.sudoku_thebestone.view.ThemeSelectorView;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

public class Gameplay extends AppCompatActivity {

    public List<Integer> mGridSizes;
    public int mCurrentLevel;
    public GameType mGameMode;
    public Sudoku           mSudoku;
    public SudokuTypes      mSudokuType;

    public SudokuData       mSudokuData;

    public SamuraiSudoku.SudokuType mSamuraiType;
    public ThemeSelectorView mThemeSelectorView;
    public GameplayView mActivityView;

    public SamuraiGridType mGridType;

    private AsyncSudokuCreator mCreator;
    private Boolean mIsDarkMode;

    private DailyChallengeData mDailyChallengeData;

    private boolean isFirstRun = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gameplay);

        Bundle bundle = getIntent().getExtras();
        int ordinal = bundle.getInt(Keys.GAMEPLAY_TYPE);
        GamePlayType gamePlayType = GamePlayType.values()[ordinal];

        mActivityView = new GameplayView(this);

        mIsDarkMode = bundle.getBoolean(Keys.DARK_MODE);

        setGlobalValues(bundle);

        if(mSudokuType == SudokuTypes.DAILY_CHALLENGE) {
            mDailyChallengeData = new DailyChallengeData(this.getApplicationContext());
        }

        switch (gamePlayType) {
            case NEW_GAME:
                newGame();
                break;
            case RESUME_GAME:
                resumeGame();
                break;
        }

        Log.e("GamePlay_Activity", "Finished Loading");
    }

    private void setGlobalValues(Bundle bundle) {
        mGridSizes          = bundle.getIntegerArrayList(Keys.GRID_SIZE);
        mCurrentLevel       = bundle.getInt(Keys.GAME_LEVEL);

        int ordinal         = bundle.getInt(Keys.GAME_MODE);
        int ordinal_type    = bundle.getInt(Keys.SUDOKU_TYPE);
        int ordinal_grid    = bundle.getInt(Keys.GRID_TYPE);

        mGameMode           = GameType.values()[ordinal];
        mSudokuType         = SudokuTypes.values()[ordinal_type];
        mGridType           = SamuraiGridType.values()[ordinal_grid];
    }

    private void newGame() {
        int gridSize = 3;
        Level level     = getCurrentLevel();

        if(mSudokuType == SudokuTypes.DAILY_CHALLENGE) {
            mSudokuData     = new SudokuData(this.getApplicationContext(), mSudokuType);
            mDailyChallengeData.setCreatedDate();
            SudokuTypes type = SudokuTypes.values()[mDailyChallengeData.getSudokuType()];
            gridSize    = mDailyChallengeData.getGridSize();
            mSudoku     = getSudoku(gridSize, type);

            mCreator = new AsyncSudokuCreator(
                    this,
                    mSudoku,
                    mSudokuData,
                    level,
                    type
            );
        }
        else {
            mSudokuData     = new SudokuData(this.getApplicationContext(), mGridType, mGameMode, mSudokuType);
            gridSize    = getRandomGridSize();
            if(mSudokuType == SudokuTypes.SAMURAI) {
                gridSize = 3;
            }
            mSudoku = getSudoku(gridSize, mSudokuType);

            mCreator = new AsyncSudokuCreator(
                    this,
                    mSudoku,
                    mSudokuData,
                    level,
                    mSudokuType
            );
        }

        mSudokuData.getBoardData().setSudokuSize(gridSize);
        mSudokuData.getBoardData().setTime(0);

        mCreator.execute(CreatorType.NEW_GAME);
    }

    private void resumeGame() {
        int gridSize = 0;

        if(mSudokuType == SudokuTypes.DAILY_CHALLENGE) {
            mSudokuData         = new SudokuData(this, mSudokuType);
            gridSize            = mSudokuData.getBoardData().getSudokuSize();
            SudokuTypes type    = SudokuTypes.values()[mDailyChallengeData.getSudokuType()];
            mSudoku = getSudoku(mDailyChallengeData.getGridSize(), type);
        }
        else {
            mSudokuData         = new SudokuData(this, mGridType, mGameMode, mSudokuType);
            gridSize = mSudokuData.getBoardData().getSudokuSize();
            mSudoku = getSudoku(gridSize, mSudokuType);
        }

        if(mSudokuData.getBoardData().isCompleted()) {
            mSudokuData     = new SudokuData(this.getApplicationContext(), mGridType, mGameMode, mSudokuType);
            gridSize    = getRandomGridSize();
            mCurrentLevel++;
            Level level     = getCurrentLevel();
            if(mSudokuType == SudokuTypes.SAMURAI) {
                gridSize = 3;
            }
            mSudoku = getSudoku(gridSize, mSudokuType);

            mCreator = new AsyncSudokuCreator(
                    this,
                    mSudoku,
                    mSudokuData,
                    level,
                    mSudokuType
            );

            mSudokuData.getBoardData().onClear();
            mCreator.execute(CreatorType.NEW_GAME);
        }
        else {

            mActivityView.setTime(getTime());

            mCreator = new AsyncSudokuCreator(
                    this,
                    mSudoku,
                    mSudokuData,
                    null,
                    null
            );

            mCreator.execute(CreatorType.LOAD_GAME);
        }
    }

    public void startNextLevel() {
        Log.e("GamePlayActivty", "Start New Level");
        mCurrentLevel++;

        mActivityView.fadeOut();

        if(mCreator != null) {
            mCreator.cancel(true);
        }

        mSudokuData.getBoardData().setTime(0);

        mCreator = new AsyncSudokuCreator(
                this,
                mSudoku,
                mSudokuData,
                getCurrentLevel(),
                mSudokuType
        );

        mCreator.execute(CreatorType.NEW_GAME);
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    public void onConfigurationChanged(@NonNull @NotNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        int NIGHT_MODE_FLAGS = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if(NIGHT_MODE_FLAGS == Configuration.UI_MODE_NIGHT_YES) {
            mActivityView.setDayNightTheme(true);
        }
    }

    public int getRandomGridSize() {
        if(mGridSizes == null)
            return 2;

        if(mGridSizes.size() == 0)
            return 2;

        Collections.shuffle(mGridSizes);
        return mGridSizes.get(0);
    }

    public Level getCurrentLevel() {

        if(mCurrentLevel > 30) {
            return Level.INSANE;
        }
        else if (mCurrentLevel > 20) {
            return Level.EXTREME;
        }
        else if (mCurrentLevel > 12) {
            return Level.HARD;
        }
        else if(mCurrentLevel > 5) {
            return Level.MEDIUM;
        }
        else {
            return Level.EASY;
        }
    }

    public  Sudoku getSudoku() {
        return this.mSudoku;
    }

    private Sudoku getSudoku(int grid, SudokuTypes sudokuType) {
        switch (sudokuType) {
            case SAMURAI:
                return new SamuraiSudokuTest(mGridType).getSudoku();

            default:
                return new StandardSudoku(grid).getSudoku();
        }
    }

    public void saveCurrentBoard() {
        mSudokuData.getBoardData().setCurrentBoard(mSudoku.getGrids());
    }

    public void setTime(int second) {
        mSudokuData.getBoardData().setTime(second);
    }

    public void onSudokuCreated(Boolean isCreated) {
        if(isCreated) {
            mSudokuData.getBoardData().setCurrentLevel(mCurrentLevel);

            if(!isFirstRun) {
                mActivityView.resetTime();
                mSudokuData.getBoardData().onClear();
            }

            mActivityView.createBoardView();

            if(mSudokuType == SudokuTypes.DAILY_CHALLENGE) {
                mDailyChallengeData.setSudokuCreated(true);
            }

            if(isFirstRun) {
                initTheme();
                isFirstRun = false;
            }
            else {
                initTheme();

                mActivityView.hideFinish();
            }

            mActivityView.fadeIn();
        }

        Log.e("Gameplay", "Sudoku Created : " + isCreated);
    }

    private void initTheme() {
        Bundle bundle       = getIntent().getExtras();
        int theme           = bundle.getInt(Keys.GAME_THEME);

        mThemeSelectorView  = new ThemeSelectorView(
                getApplicationContext(),
                findViewById(R.id.layout_themeMenu),
                mActivityView
        );

        mThemeSelectorView.selectTheme(theme);

        int NIGHT_MODE_FLAGS = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if(NIGHT_MODE_FLAGS == Configuration.UI_MODE_NIGHT_YES || mIsDarkMode) {
            mActivityView.setDayNightTheme(true);
        }
    }

    public int getTime() {
        if(mSudokuData.isSaved()) {
            return mSudokuData.getBoardData().getTime();
        }
        else {
            return 0;
        }
    }

    public boolean hasNextLevel() {
        return mSudokuType != SudokuTypes.DAILY_CHALLENGE;
    }

    @Override
    protected void onDestroy() {
        mCreator.cancel(true);
        mActivityView.onDestroy();
        super.onDestroy();

    }

    public void setIsCompleted() {
        if(mDailyChallengeData != null) {
            mDailyChallengeData.setIsCompleted(true);
        }
    }

    public void saveLevel() {
        com.realgear.sudoku_thebestone.core.Level level = new com.realgear.sudoku_thebestone.core.Level();
        level.putData(LevelEnums.Level, String.valueOf(mCurrentLevel));
        level.putData(LevelEnums.Time, String.valueOf(getTime()));
        level.putData(LevelEnums.Hints_Used, String.valueOf(mSudokuData.getBoardData().getHintsUsed()));
        level.putData(LevelEnums.Checks_Used, String.valueOf(mSudokuData.getBoardData().getChecksUsed()));
        level.putData(LevelEnums.Started_Date, mSudokuData.getBoardData().getCreatedDate());
        level.putData(LevelEnums.Completed_Date, LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE));

        LevelData levelData = new LevelData(this);
        levelData.saveLevel(mGameMode, mSudokuType, mGridType, level);
    }
}