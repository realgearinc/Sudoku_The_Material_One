package com.realgear.sudoku_thebestone.view;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.realgear.sudoku_thebestone.MainActivity;
import com.realgear.sudoku_thebestone.R;
import com.realgear.sudoku_thebestone.activities.ActivityEnums;
import com.realgear.sudoku_thebestone.adapters.StateFragmentAdapter;
import com.realgear.sudoku_thebestone.core.GameMode;
import com.realgear.sudoku_thebestone.core.GameType;
import com.realgear.sudoku_thebestone.core.SudokuTypes;
import com.realgear.sudoku_thebestone.core.TimeHandler;
import com.realgear.sudoku_thebestone.data.DailyChallengeData;
import com.realgear.sudoku_thebestone.data.MainActivityData;
import com.realgear.sudoku_thebestone.fragments.GameModeFragment;
import com.realgear.sudoku_thebestone.fragments.SudokuTypeFragment;
import com.realgear.sudoku_thebestone.theme.Theme;
import com.realgear.sudoku_thebestone.theme.Themeable;
import com.realgear.sudoku_thebestone.theme.Themes;
import com.realgear.sudoku_thebestone.utils.Action;
import com.realgear.sudoku_thebestone.utils.GamePlayType;
import com.realgear.sudoku_thebestone.utils.Keys;
import com.realgear.sudoku_thebestone.utils.SamuraiGridType;
import com.realgear.sudoku_thebestone.utils.Time;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivityView extends Themeable  {
    private final String TAG = MainActivityView.class.getSimpleName();

    private FloatingActionButton mBtn_PrevGameMode, mBtn_NextGameMode, mBtn_PrevGridType, mBtn_NextGridType, mBtn_PrevGameType, mBtn_NextGameType, mBtn_DayNight;
    private Button mBtn_NewGame, mBtn_ResumeGame;

    private FloatingActionButton mBtn_Settings,
                                 mBtn_Shop,
                                 mBtn_Statistics,
                                 mBtn_Info;

    private FrameLayout mFrame_NewGame, mFrame_ResumeGame;

    private ImageView mImage_Icon;

    private ViewPager2 mViewPager;
    private final List<GameMode> mGameModes;

    private ViewPager2 mViewPager_SudokuGrid;
    private final List<SamuraiGridType> mGridTypes;

    private ViewPager2 mViewPager_SudokuTypes;
    private final List<SudokuTypes> mSudokuTypes;

    private boolean mIsDarkTheme;

    private View mGameModesFrame;
    private View mGridTypesFrame;
    private View mSudokuTypesFrame;


    private final MainActivity mActivity;
    private final MainActivityData mActivityData;
    private final DailyChallengeData mDailyChallengeData;
    public final boolean isFirstRun;
    public final int isFirstTheme;

    private final TimeHandler mTimeHandler;
    private final int mPrevTime;

    public MainActivityView(MainActivity mainActivity) {
        this.mActivity      = mainActivity;
        this.mGameModes     = new ArrayList<>();
        this.mSudokuTypes   = new ArrayList<>();
        this.mGridTypes     = new ArrayList<>();

        this.mActivityData          = new MainActivityData(mActivity);
        this.mDailyChallengeData    = new DailyChallengeData(mActivity.getApplicationContext());
        this.mTimeHandler           = new TimeHandler();

        LocalTime time = LocalTime.now();
        mPrevTime = time.toSecondOfDay();

        mTimeHandler.setTimer(new Time() {
            @Override
            public void onTick(int second) {
                int totalSeconds = mPrevTime + second;
                int left = (((24 * 60) * 60) - totalSeconds);
                mBtn_NewGame.setText(mTimeHandler.getTime(left));
            }
        });

        initUI();
        initGameModeViewPager();
        initSudokuTypeViewPager();
        initSudokuGridViewPager();

        Log.e(TAG, "Is First Run ? " + mActivityData.isFirstRun());

        isFirstRun = mActivityData.isFirstRun();

        if(!isFirstRun) {
            int theme   = mActivityData.getLastSelectedTheme();
            int s_type  = mActivityData.getLastSelectedSudoku();
            int s_level = mActivityData.getLastSelectedLevel();
            int s_grid  = mActivityData.getLastSelectedGrid();

            mViewPager_SudokuTypes.setCurrentItem(s_type, false);
            mViewPager.setCurrentItem(s_level, false);
            mViewPager_SudokuGrid.setCurrentItem(s_grid, false);
            isFirstTheme = theme;
        }
        else {
            mActivityData.setFirstRun();
            mViewPager_SudokuTypes.setCurrentItem(0, false);
            mViewPager_SudokuGrid.setCurrentItem(0, false);
            mViewPager.setCurrentItem(0, false);

            isFirstTheme = 0;
        }
    }

    public MainActivityData getActivityData() {
        return this.mActivityData;
    }

    public void initUI() {
        mBtn_PrevGameMode   = getFabById(R.id.btn_prev_game_mode);
        mBtn_NextGameMode   = getFabById(R.id.btn_next_game_mode);

        mBtn_PrevGridType = getFabById(R.id.btn_prev_sudoku_grid);
        mBtn_NextGridType = getFabById(R.id.btn_next_sudoku_grid);

        mBtn_NextGameType = getFabById(R.id.btn_next_sudoku_mode);
        mBtn_PrevGameType = getFabById(R.id.btn_prev_sudoku_mode);

        mBtn_NewGame    = getBtnById(R.id.btn_new_game);
        mBtn_ResumeGame = getBtnById(R.id.btn_resume_game);

        mBtn_Settings   = getFabById(R.id.btn_settings);
        mBtn_Shop       = getFabById(R.id.btn_shop);
        mBtn_Statistics = getFabById(R.id.btn_statistics);
        mBtn_Info       = getFabById(R.id.btn_info);
        mBtn_DayNight = getFabById(R.id.btn_daynight);

        mImage_Icon     = (ImageView) findViewById(R.id.ImageView_icon);

        mFrame_NewGame = (FrameLayout) findViewById(R.id.frame_new_game);
        mFrame_ResumeGame = (FrameLayout)findViewById(R.id.frame_resume_game);

        mGameModesFrame = findViewById(R.id.linearLayout);
        mGridTypesFrame = findViewById(R.id.linearLayout_sudokuGrid);
        mSudokuTypesFrame = findViewById(R.id.linearLayout_sudoku);

        onTouchListener(mBtn_NewGame,
                () -> {
                    mFrame_NewGame.setBackgroundTintList(ColorStateList.valueOf(getTheme().getPrimaryColor()));
                    mBtn_NewGame.setTextColor(Color.WHITE);
                },
                () -> {
                    mFrame_NewGame.setBackgroundTintList(ColorStateList.valueOf(Color.argb(0, 0, 0,0)));
                    mBtn_NewGame.setTextColor(getTheme().getPrimaryColor());
                });

        onTouchListener(mBtn_ResumeGame,
                () -> {
                    mFrame_ResumeGame.setBackgroundTintList(ColorStateList.valueOf(getTheme().getPrimaryColor()));
                    mBtn_ResumeGame.setTextColor(Color.WHITE);
                },
                () -> {
                    mFrame_ResumeGame.setBackgroundTintList(ColorStateList.valueOf(Color.argb(0, 0, 0,0)));
                    mBtn_ResumeGame.setTextColor(getTheme().getPrimaryColor());
                });

        addListener(mBtn_PrevGameMode, this::onClickPrevGameMode);
        addListener(mBtn_NextGameMode, this::onClickNextGameMode);
        addListener(mBtn_NextGridType, this::onClickNextGridType);
        addListener(mBtn_PrevGridType, this::onClickPrevGridType);
        addListener(mBtn_NextGameType, this::onClickNextGameType);
        addListener(mBtn_PrevGameType, this::onClickPrevGameType);

        addListener(mBtn_NewGame, this::onClickNewGame);
        addListener(mBtn_ResumeGame, this::onClickResumeGame);
        addListener(mBtn_Settings, this::onClickSettings);
        addListener(mBtn_Shop, this::onClickShop);
        addListener(mBtn_Statistics, this::onClickStatistics);
        addListener(mBtn_Info, this::onClickInfo);
        addListener(mBtn_DayNight, () -> {
            onClickDayNight(mBtn_DayNight);
        });

        mViewPager = (ViewPager2) findViewById(R.id.viewPager_game_mode);
        mViewPager_SudokuTypes = (ViewPager2)findViewById(R.id.viewPager_sudoku_mode);
        mViewPager_SudokuGrid = (ViewPager2)findViewById(R.id.viewPager_sudoku_grid);
    }

    public void initSudokuTypeViewPager() {
        mSudokuTypes.add(SudokuTypes.STANDARD);
        mSudokuTypes.add(SudokuTypes.SAMURAI);
        mSudokuTypes.add(SudokuTypes.DAILY_CHALLENGE);

        if(mViewPager_SudokuTypes != null) {
            StateFragmentAdapter adapter = new StateFragmentAdapter(mActivity.getSupportFragmentManager(), mActivity.getLifecycle());
            for(int i = 0; i < mSudokuTypes.size(); i++) {
                adapter.addFragment(new SudokuTypeFragment(mSudokuTypes.get(i).toString().replace("_", " ")));
            }
            mViewPager_SudokuTypes.setAdapter(adapter);
            mViewPager_SudokuTypes.setOffscreenPageLimit(adapter.getItemCount());

            mViewPager_SudokuTypes.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                @Override
                public void onPageSelected(int position) {
                    super.onPageSelected(position);
                    onGameTypeChanged();
                }
            });
        }
    }

    public void initGameModeViewPager() {
        mGameModes.add(new GameMode(GameType.Beginner));   //2 Easy - Medium
        mGameModes.add(new GameMode(GameType.Easy));       //2, 3 Medium - Hard
        mGameModes.add(new GameMode(GameType.Medium));     //2, 3 Hard - Extreme
        mGameModes.add(new GameMode(GameType.Hard));       //3
        mGameModes.add(new GameMode(GameType.Extreme));    //4
        mGameModes.add(new GameMode(GameType.Insane));     //5

        mViewPager = (ViewPager2) findViewById(R.id.viewPager_game_mode);
        if(mViewPager != null) {
            StateFragmentAdapter adapter1 = new StateFragmentAdapter(mActivity.getSupportFragmentManager(), mActivity.getLifecycle());

            for(int i = 0; i < mGameModes.size(); i++) {
                adapter1.addFragment(new GameModeFragment(mGameModes.get(i).mGameType.toString(), mGameModes.get(i).getCurrentLevel()));
            }

            mViewPager.setAdapter(adapter1);
            mViewPager.setOffscreenPageLimit(adapter1.getItemCount());
        }

        if(mViewPager == null)
            return;

        mViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                onGameTypeChanged();
                super.onPageSelected(position);
            }
        });

        mViewPager.setCurrentItem(0);
    }

    public void initSudokuGridViewPager() {
        mGridTypes.add(SamuraiGridType.GRID_2);
        mGridTypes.add(SamuraiGridType.GRID_3);
        mGridTypes.add(SamuraiGridType.GRID_4);
        mGridTypes.add(SamuraiGridType.GRID_5);
        mGridTypes.add(SamuraiGridType.GRID_2_GATTAI);
        mGridTypes.add(SamuraiGridType.GRID_3_GATTAI);
        mGridTypes.add(SamuraiGridType.GRID_4_GATTAI);
        mGridTypes.add(SamuraiGridType.GRID_5_GATTAI);
        mGridTypes.add(SamuraiGridType.SUPER);

        if(mViewPager_SudokuGrid != null) {
            StateFragmentAdapter adapter = new StateFragmentAdapter(mActivity.getSupportFragmentManager(), mActivity.getLifecycle());
            for(int i = 0; i < mGridTypes.size(); i++) {
                adapter.addFragment(new SudokuTypeFragment(mGridTypes.get(i).toString().replace("_", " ")));
            }

            mViewPager_SudokuGrid.setAdapter(adapter);
            mViewPager_SudokuGrid.setOffscreenPageLimit(adapter.getItemCount());

            mViewPager_SudokuGrid.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                @Override
                public void onPageSelected(int position) {
                    super.onPageSelected(position);
                    onGameTypeChanged();
                }
            });
        }
    }

    private void onGameTypeChanged() {
        //Reload A Few Things
        SudokuTypes type            = SudokuTypes.values()[mViewPager_SudokuTypes.getCurrentItem()];
        GameType level              = GameType.values()[mViewPager.getCurrentItem()];
        SamuraiGridType gridType    = SamuraiGridType.values()[mViewPager_SudokuGrid.getCurrentItem()];

        boolean isSaved = mActivity.getSudokuData(gridType, level, type).isSaved();
        int curLevel = mActivity.getSudokuData(gridType, level, type).getBoardData().getCurrentLevel();

        GameModeFragment fragment = (GameModeFragment)((StateFragmentAdapter)mViewPager.getAdapter()).getItem(mViewPager.getCurrentItem());


        if(fragment != null) {
            fragment.setLevel((curLevel > 0) ? curLevel : 1);
            mViewPager.getAdapter().notifyItemChanged(mViewPager.getCurrentItem());
        }


        int duration = 350;

        if(type != SudokuTypes.DAILY_CHALLENGE) {
            mBtn_ResumeGame.setClickable(true);
            mBtn_ResumeGame.setText("Resume");

            mTimeHandler.stop();
            mBtn_NewGame.setText("New Game");

            if(type == SudokuTypes.STANDARD) {
                if(mGridTypesFrame.getAlpha() > 0.0f) {
                    startFading(mGridTypesFrame, 1.0f, 0.0f, duration, false);
                }
            }
            else {
                if(mGridTypesFrame.getAlpha() < 1.0f) {
                    startFading(mGridTypesFrame, 0.0f, 1.0f, duration, false);
                }
            }

            if(mGameModesFrame.getAlpha() < 1.0f) {
                startFading(mGameModesFrame, 0.0f, 1.0f, duration, false);
            }

            switch (type) {
                case SAMURAI:
                {
                    if(mViewPager_SudokuGrid.getVisibility() == View.INVISIBLE) {
                        startFading(mViewPager_SudokuGrid, 0.0f, 1.0f, duration, false);
                    }
                }
            }

            if(isSaved && mBtn_ResumeGame.getVisibility() == View.INVISIBLE) {
                startFading(mBtn_ResumeGame, 0.0f, 1.0f, duration, false);
            }
            else if(!isSaved && mBtn_ResumeGame.getVisibility() == View.VISIBLE) {
                startFading(mBtn_ResumeGame, 1.0f, 0.0f, duration, false);
            }
        }
        else {
            mTimeHandler.start();
            if(mGridTypesFrame.getAlpha() > 0.0f) {
                startFading(mGridTypesFrame, 1.0f, 0.0f, duration, false);
            }

            startFading(mGameModesFrame, 1.0f, 0.0f, duration, false);

            if(mBtn_ResumeGame.getVisibility() == View.INVISIBLE) {
                startFading(mBtn_ResumeGame, 0.0f, 1.0f, duration, false);
            }

            if(mDailyChallengeData.isCompleted()) {
                mBtn_ResumeGame.setText("Completed");
                mBtn_ResumeGame.setClickable(false);
            }
            else {
                mBtn_ResumeGame.setText("Resume");
                mBtn_ResumeGame.setClickable(true);
            }
        }

        mActivityData.setLastSelectedSudoku(type.ordinal());
        mActivityData.setLastSelectedLevel(level.ordinal());
        mActivityData.setLastSelectedGrid(gridType.ordinal());
    }

    private void addListener(Object button, Action action) {
        if(button == null)
            return;

        try {
            FloatingActionButton btn = (FloatingActionButton)button;
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        action.call();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            try {
                Button btn = (Button) button;
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            action.call();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            } catch (Exception x) {}
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void onTouchListener(Button button, Action onTouchDownAction, Action onTouchUpAction) {
        if(button == null) {
            return;
        }

        button.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                try {
                    switch (motionEvent.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                        case MotionEvent.ACTION_HOVER_ENTER:
                            onTouchDownAction.call();
                            break;
                        case MotionEvent.ACTION_UP:
                        case MotionEvent.ACTION_CANCEL:
                        case MotionEvent.ACTION_HOVER_EXIT:
                            onTouchUpAction.call();
                            break;

                        default:
                            return false;

                    }
                }
                catch (Exception e) {

                }
                return false;
            }
        });
    }

    private void onClickPrevGameMode() {
        if(mViewPager == null)
            return;

        if(mViewPager.getAdapter().getItemCount() == 0)
            return;

        int position = mViewPager.getCurrentItem() - 1;
        if(position >= 0) {
            mViewPager.setCurrentItem(position);
        }
        else {
            mViewPager.setCurrentItem(mViewPager.getAdapter().getItemCount() - 1);
        }
    }
    private void onClickNextGameMode() {
        if(mViewPager == null)
            return;

        if(mViewPager.getAdapter().getItemCount() == 0)
            return;

        int position = mViewPager.getCurrentItem() + 1;
        if(position < mViewPager.getAdapter().getItemCount()) {
            mViewPager.setCurrentItem(position);
        }
        else {
            mViewPager.setCurrentItem(0);
        }

    }

    private void onClickNextGridType() {
        if(mViewPager_SudokuGrid == null || mViewPager_SudokuGrid.getAdapter().getItemCount() <= 1)
            return;

        int position = mViewPager_SudokuGrid.getCurrentItem() + 1;
        if(position < mViewPager_SudokuGrid.getAdapter().getItemCount()) {
            mViewPager_SudokuGrid.setCurrentItem(position);
        }
        else {
            mViewPager_SudokuGrid.setCurrentItem(0);
        }
    }
    private void onClickPrevGridType() {
        if(mViewPager_SudokuGrid == null || mViewPager_SudokuGrid.getAdapter().getItemCount() <= 1)
            return;

        int position = mViewPager_SudokuGrid.getCurrentItem() - 1;
        if(position >= 0) {
            mViewPager_SudokuGrid.setCurrentItem(position);
        }
        else {
            mViewPager_SudokuGrid.setCurrentItem(mViewPager_SudokuGrid.getAdapter().getItemCount() - 1);
        }
    }

    private void onClickNextGameType() {
        if(mViewPager_SudokuTypes == null)
            return;

        if(mViewPager_SudokuTypes.getAdapter().getItemCount() == 0)
            return;

        int position = mViewPager_SudokuTypes.getCurrentItem() + 1;
        if(position < mViewPager_SudokuTypes.getAdapter().getItemCount()) {
            mViewPager_SudokuTypes.setCurrentItem(position);
        }
        else {
            mViewPager_SudokuTypes.setCurrentItem(0);
        }
    }
    private void onClickPrevGameType() {
        if(mViewPager_SudokuTypes == null)
            return;

        if(mViewPager_SudokuTypes.getAdapter().getItemCount() <= 1)
            return;

        int position = mViewPager_SudokuTypes.getCurrentItem() - 1;
        if(position >= 0) {
            mViewPager_SudokuTypes.setCurrentItem(position);
        }
        else {
            mViewPager_SudokuTypes.setCurrentItem(mViewPager_SudokuTypes.getAdapter().getItemCount() - 1);
        }
    }

    private void onClickNewGame() {
        GameMode curGameMode = mGameModes.get(mViewPager.getCurrentItem());
        SudokuTypes curSudokuType = mSudokuTypes.get(mViewPager_SudokuTypes.getCurrentItem());
        GameType level              = GameType.values()[mViewPager.getCurrentItem()];
        SamuraiGridType curGridType = mGridTypes.get(mViewPager_SudokuGrid.getCurrentItem());

        if(curGameMode == null)
            return;

        if(curSudokuType == SudokuTypes.DAILY_CHALLENGE)
            return;

        boolean isSaved = mActivity.getSudokuData(curGridType, level, curSudokuType).isSaved();

        Action action = new Action() {
            @Override
            public void call() throws Exception {
                Bundle bundle = new Bundle();
                bundle.putInt(Keys.GAMEPLAY_TYPE, GamePlayType.NEW_GAME.ordinal());

                bundle.putIntegerArrayList(Keys.GRID_SIZE, getGridSize(curSudokuType));
                bundle.putInt(Keys.GAME_LEVEL,  1);
                bundle.putInt(Keys.GAME_THEME,  getTheme().getOrdinal());
                bundle.putInt(Keys.GAME_MODE,   curGameMode.mGameType.ordinal());
                bundle.putInt(Keys.SUDOKU_TYPE, curSudokuType.ordinal());
                bundle.putInt(Keys.GRID_TYPE,   curGridType.ordinal());
                bundle.putBoolean(Keys.DARK_MODE, mIsDarkTheme);

                startActivity(ActivityEnums.GAMEPLAY_ACTIVITY, bundle);
            }
        };

        if(isSaved) {
            LayoutInflater inflater = (LayoutInflater)mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View parent = mActivity.getWindow().getDecorView().getRootView();

            ConfirmLayoutView confirmLayoutView = new ConfirmLayoutView(
                    inflater,
                    parent,
                    action,
                    "New Game",
                    getTheme()
            );

            TextView info = new TextView(mActivity.getApplicationContext());
            info.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            info.setText("All of your current progress will be lost.\nAre you sure you want to start a new game ?");
            info.setTextSize(18);
            confirmLayoutView.addText(info);

            confirmLayoutView.show();
        }
        else {
            try {
                action.call();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private ArrayList<Integer> getGridSize(SudokuTypes sudokuType) {
        List<Integer> result = new ArrayList<>();
        if(sudokuType == SudokuTypes.SAMURAI) {
            result.add(3);
            //result.add(3);
        }
        else if(sudokuType == SudokuTypes.STANDARD) {
            GameMode curGameMode = mGameModes.get(mViewPager.getCurrentItem());
            result.addAll(curGameMode.getGridSize());
        }

        Collections.shuffle(result);
        return new ArrayList<>(result);
    }

    private void onClickResumeGame() {
        GameMode curGameMode = mGameModes.get(mViewPager.getCurrentItem());
        SudokuTypes curSudokuType = mSudokuTypes.get(mViewPager_SudokuTypes.getCurrentItem());
        SamuraiGridType curGridType = mGridTypes.get(mViewPager_SudokuGrid.getCurrentItem());

        if(curGameMode == null)
            return;

        Bundle bundle = new Bundle();

        if(curSudokuType == SudokuTypes.DAILY_CHALLENGE) {
            if(mDailyChallengeData.isCompleted())
                return;

            if(mDailyChallengeData.canResume()) {
                bundle.putInt(Keys.GAMEPLAY_TYPE, GamePlayType.RESUME_GAME.ordinal());

                bundle.putInt(Keys.GRID_SIZE,  mDailyChallengeData.getGridSize());
                bundle.putInt(Keys.GAME_LEVEL,  mDailyChallengeData.getLevel());
                bundle.putInt(Keys.GAME_MODE,   mDailyChallengeData.getGameMode());
                bundle.putInt(Keys.SUDOKU_TYPE, curSudokuType.ordinal());
                bundle.putInt(Keys.GRID_TYPE,   mDailyChallengeData.getGridType());
            }
            else {
                mDailyChallengeData.setSudoku();
                bundle.putInt(Keys.GAMEPLAY_TYPE, GamePlayType.NEW_GAME.ordinal());

                bundle.putInt(Keys.GRID_SIZE, mDailyChallengeData.getGridSize());
                bundle.putInt(Keys.GAME_LEVEL,  1);
                bundle.putInt(Keys.GAME_MODE,   mDailyChallengeData.getGameMode());
                bundle.putInt(Keys.SUDOKU_TYPE, curSudokuType.ordinal());
                bundle.putInt(Keys.GRID_TYPE,   mDailyChallengeData.getGridType());
            }
        }
        else {

            int curLevel = mActivity.getSudokuData(curGridType, curGameMode.mGameType, curSudokuType).getBoardData().getCurrentLevel();

            bundle.putInt(Keys.GAMEPLAY_TYPE, GamePlayType.RESUME_GAME.ordinal());
            bundle.putInt(Keys.GAME_MODE, curGameMode.mGameType.ordinal());
            bundle.putInt(Keys.GAME_LEVEL, curLevel);
            bundle.putInt(Keys.SUDOKU_TYPE, curSudokuType.ordinal());
            bundle.putInt(Keys.GRID_TYPE, curGridType.ordinal());
        }

        bundle.putInt(Keys.GAME_THEME, getTheme().getOrdinal());
        bundle.putBoolean(Keys.DARK_MODE, mIsDarkTheme);

        startActivity(ActivityEnums.GAMEPLAY_ACTIVITY, bundle);
    }

    private void onClickSettings() {
        startActivity(ActivityEnums.SETTINGS_ACTIVITY, null);
    }

    private void onClickShop() {
        startActivity(ActivityEnums.PLAYSTORE_ACTIVITY, null);
    }

    private void onClickStatistics() {
        Bundle bundle = new Bundle();

        bundle.putInt(Keys.GAME_THEME, getTheme().getOrdinal());

        startActivity(ActivityEnums.STATISTICS_ACTIVITY, bundle);
    }

    private void onClickInfo() {
        startActivity(ActivityEnums.ABOUT_ACTIVITY, null);
    }

    private void onClickDayNight(View v) {
        //AppSettings
        setDayNightTheme(!mIsDarkTheme);
    }

    private void startActivity(ActivityEnums activty, Bundle bundle) {
        this.mActivity.runActivity(activty, bundle);
    }

    public void setDayNightTheme(boolean isDarkMode) {
        if(mIsDarkTheme == isDarkMode)
            return;

        if(isDarkMode) {
            if(getTheme().getOrdinal() == Themes.FOREST_GREEN_WHITE.ordinal() || getTheme().getOrdinal() == Themes.GREY_WHITE.ordinal()) {
                return;
            }

            int black_bg = ContextCompat.getColor(mActivity, R.color.black_grey);
            int white_bg = getTheme().getBackgroundColor();

            ValueAnimator valueAnimator = getValueAnimator(white_bg, black_bg);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    mActivity.getWindow().getDecorView().getRootView().setBackgroundColor((int) valueAnimator.getAnimatedValue());
                    //mActivity.getWindow().setStatusBarColor((int) valueAnimator.getAnimatedValue());
                    setStatusBarColor((int) valueAnimator.getAnimatedValue(), false);
                }
            });
            valueAnimator.start();


            mIsDarkTheme = true;
            Log.e("MainActivityView", "Dark Mode Set");
        }
        else {
            int black_bg = ContextCompat.getColor(mActivity, R.color.black_grey);
            int white_bg = Color.WHITE;

            ValueAnimator valueAnimator = getValueAnimator(black_bg, white_bg);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    mActivity.getWindow().getDecorView().getRootView().setBackgroundColor((int) valueAnimator.getAnimatedValue());
                    setStatusBarColor((int) valueAnimator.getAnimatedValue(), true);
                }
            });
            valueAnimator.start();

            //mActivity.getWindow().getDecorView().
            mIsDarkTheme = false;
            Log.e("MainActivityView", "Light Mode Set");
        }
    }

    private void setStatusBarColor(Integer color, boolean darkStatusBarTint) {
        Activity activity = mActivity;
        Window window = activity.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(color);
        int flag = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        window.getDecorView().setSystemUiVisibility(darkStatusBarTint ? flag : 0);
    }

    private ValueAnimator getValueAnimator(int from, int to) {
        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), from, to);
        colorAnimation.setDuration(350);
        return colorAnimation;
    }

    int curTheme = -1;

    @Override
    public void setTheme(Theme theme) {
        super.setTheme(theme);
    }

    @Override
    public void updateTheme() {
        if(curTheme == -1) {
            curTheme = mActivity.getThemeSelectorView().getCurrentTheme();
            mActivityData.setLastSelectedTheme(curTheme);
        }
        else if(curTheme != mActivity.getThemeSelectorView().getCurrentTheme()) {
            curTheme = mActivity.getThemeSelectorView().getCurrentTheme();
            mActivityData.setLastSelectedTheme(curTheme);
        }

        //mActivityData.setLastSelectedTheme(getTheme().getOrdinal());

        if(!mIsDarkTheme) {
            mActivity.getWindow().getDecorView().getRootView().setBackgroundColor(getTheme().getBackgroundColor());
            mActivity.getWindow().setStatusBarColor(getTheme().getBackgroundColor());

            if(getTheme().getBackgroundColor() == Color.WHITE) {
                setStatusBarColor(getTheme().getBackgroundColor(), true);
            }
            else
                setStatusBarColor(getTheme().getBackgroundColor(), false);
        }

        mImage_Icon.setImageTintList(ColorStateList.valueOf(getTheme().getPrimaryColor()));

        mBtn_ResumeGame.setBackgroundTintList(ColorStateList.valueOf(getTheme().getPrimaryColor()));
        mBtn_ResumeGame.setTextColor(getTheme().getPrimaryColor());

        mBtn_NewGame.setBackgroundTintList(ColorStateList.valueOf(getTheme().getPrimaryColor()));
        mBtn_NewGame.setTextColor(getTheme().getPrimaryColor());

        mBtn_NextGameType.setSupportImageTintList(ColorStateList.valueOf(getTheme().getPrimaryColor()));
        mBtn_PrevGameType.setSupportImageTintList(ColorStateList.valueOf(getTheme().getPrimaryColor()));
        mBtn_NextGridType.setSupportImageTintList(ColorStateList.valueOf(getTheme().getPrimaryColor()));
        mBtn_PrevGridType.setSupportImageTintList(ColorStateList.valueOf(getTheme().getPrimaryColor()));
        mBtn_NextGameMode.setSupportImageTintList(ColorStateList.valueOf(getTheme().getPrimaryColor()));
        mBtn_PrevGameMode.setSupportImageTintList(ColorStateList.valueOf(getTheme().getPrimaryColor()));


        mBtn_Settings.setSupportImageTintList(ColorStateList.valueOf(getTheme().getPrimaryColor()));
        mBtn_Shop.setSupportImageTintList(ColorStateList.valueOf(getTheme().getPrimaryColor()));
        mBtn_Statistics.setSupportImageTintList(ColorStateList.valueOf(getTheme().getPrimaryColor()));
        mBtn_Info.setSupportImageTintList(ColorStateList.valueOf(getTheme().getPrimaryColor()));
        mBtn_DayNight.setSupportImageTintList(ColorStateList.valueOf(getTheme().getPrimaryColor()));

        updateFragmentsTheme(getTheme());

    }

    public void updateFragmentsTheme(Theme theme) {

        StateFragmentAdapter adapter = (StateFragmentAdapter)mViewPager.getAdapter();
        int count = adapter.getItemCount();
        for(int i = 0; i < count; i++) {
            GameModeFragment fragment = (GameModeFragment)adapter.getItem(i);
            fragment.setTheme(theme);
        }


        adapter = (StateFragmentAdapter)mViewPager_SudokuTypes.getAdapter();
        count = adapter.getItemCount();
        for(int i = 0; i < count; i++) {
            SudokuTypeFragment fragment = (SudokuTypeFragment)adapter.getItem(i);
            fragment.setTheme(theme);
        }

        adapter = (StateFragmentAdapter)mViewPager_SudokuGrid.getAdapter();
        count = adapter.getItemCount();
        for(int i = 0; i < count; i++) {
            SudokuTypeFragment fragment = (SudokuTypeFragment)adapter.getItem(i);
            fragment.setTheme(theme);
        }
    }

    public FloatingActionButton getFabById(int id){
        View view = findViewById(id);
        if(view != null) {
            try {
                FloatingActionButton btn = (FloatingActionButton)view;
                return btn;
            }
            catch (Exception e) {
                return null;
            }
        }
        else {
            return null;
        }
    }

    public Button getBtnById(int id){
        View view = findViewById(id);
        if(view != null) {
            try {
                Button btn = (Button)view;
                return btn;
            }
            catch (Exception e) {
                return null;
            }
        }
        else {
            return null;
        }
    }

    public View findViewById(int id) {
        return mActivity.findViewById(id);
    }
}
