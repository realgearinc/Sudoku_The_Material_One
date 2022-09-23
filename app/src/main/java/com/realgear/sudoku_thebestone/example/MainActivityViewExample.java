package com.realgear.sudoku_thebestone.example;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.realgear.sudoku_thebestone.MainActivity;
import com.realgear.sudoku_thebestone.R;
import com.realgear.sudoku_thebestone.core.GameMode;
import com.realgear.sudoku_thebestone.core.GameType;
import com.realgear.sudoku_thebestone.core.SudokuTypes;
import com.realgear.sudoku_thebestone.core.TimeHandler;
import com.realgear.sudoku_thebestone.data.DailyChallengeData;
import com.realgear.sudoku_thebestone.data.MainActivityData;
import com.realgear.sudoku_thebestone.fragments.FragmentType;
import com.realgear.sudoku_thebestone.fragments.GameModeFragment;
import com.realgear.sudoku_thebestone.fragments.SudokuTypeFragment;
import com.realgear.sudoku_thebestone.theme.Themeable;
import com.realgear.sudoku_thebestone.utils.ButtonViewPager;
import com.realgear.sudoku_thebestone.utils.MaterialButton;
import com.realgear.sudoku_thebestone.utils.SamuraiGridType;
import com.realgear.sudoku_thebestone.utils.ThemeUtil;
import com.realgear.sudoku_thebestone.utils.Time;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class MainActivityViewExample extends Themeable {
    private final String TAG = MainActivityViewExample.class.getSimpleName();

    private ButtonViewPager mGridMode, mGameMode, mGameType;

    private MaterialButton       mBtn_NewGame, mBtn_ResumeGame;

    private FloatingActionButton mBtn_Settings,
                                 mBtn_Shop,
                                 mBtn_Statistics,
                                 mBtn_About,
                                 mBtn_DayNight;

    private final boolean       isFirstRun;
    private boolean             isDarkMode;

    private final int           mTheme;
    private int                 mPrevTime;

    private final MainActivityExample   mActivity;
    private final MainActivity          mMainActivity;
    private final MainActivityData      mActivityData;
    private final DailyChallengeData    mDailyChallengeData;

    private final TimeHandler           mTimeHandler;


    public MainActivityViewExample(MainActivityExample mainActivity) {
        this.mActivity      = mainActivity;
        this.mMainActivity  = mActivity.getMainActivity();

        this.mActivityData          = new MainActivityData(mActivity.getContext());
        this.mDailyChallengeData    = new DailyChallengeData(mActivity.getContext());

        this.mTimeHandler           = new TimeHandler();

        this.isFirstRun = mActivityData.isFirstRun();
        this.mTheme     = (isFirstRun) ? 0 : mActivityData.getLastSelectedTheme();

        //Init Daily Time On New Game Button
        initDailyTime();

        //Init UI
        initUI();

        //init ViewPagers
        initViewPagers();
        setViewPagers();
    }

    private void initDailyTime() {
        LocalTime time = LocalTime.now();
        mPrevTime = time.toSecondOfDay();

        mTimeHandler.setTimer(new Time() {
            @Override
            public void onTick(int second) {
                int totalSeconds = mPrevTime + second;
                int left = (((24 * 60)* 60) - totalSeconds);
                mBtn_NewGame.setText(mTimeHandler.getTime(left));
            }
        });
    }

    private void initUI() {
        mBtn_NewGame        = new MaterialButton(mActivity.getActivity(), R.id.frame_new_game, R.id.btn_new_game);
        mBtn_ResumeGame     = new MaterialButton(mActivity.getActivity(), R.id.frame_resume_game, R.id.btn_resume_game);

        mBtn_Settings   = getFabById(R.id.btn_settings);
        mBtn_Shop       = getFabById(R.id.btn_shop);
        mBtn_Statistics = getFabById(R.id.btn_statistics);
        mBtn_About      = getFabById(R.id.btn_info);
        mBtn_DayNight   = getFabById(R.id.btn_daynight);

        ThemeUtil.addListener(mBtn_NewGame,     this::onClickNewGame);
        ThemeUtil.addListener(mBtn_ResumeGame,  this::onClickResumeGame);
        ThemeUtil.addListener(mBtn_Settings,    this::onClickSettings);
        ThemeUtil.addListener(mBtn_Shop,        this::onClickShop);
        ThemeUtil.addListener(mBtn_Statistics,  this::onClickStatistics);
        ThemeUtil.addListener(mBtn_About,       this::onClickAbout);
        ThemeUtil.addListener(mBtn_DayNight,    this::onClickDayNight);
    }

    private void initViewPagers() {

        //Init SudokuType
        List<Object> sudokuTypesData = new ArrayList<>();
        List<Fragment> sudokuTypesFragments = new ArrayList<>();

        for(SudokuTypes sudokuType: SudokuTypes.values()) {
            sudokuTypesData.add(sudokuType);
            sudokuTypesFragments.add(new SudokuTypeFragment(sudokuType.name().replace("_", " ")));
        }

        //Init GameMode
        List<Object> gameModeData = new ArrayList<>();
        List<Fragment> gameModeFragments = new ArrayList<>();

        for(GameType gameType: GameType.values()) {
            GameMode gameMode = new GameMode(gameType);
            gameModeData.add(gameMode);
            gameModeFragments.add(new GameModeFragment(gameType.name(), gameMode.getCurrentLevel()));
        }

        //Init GridTypes
        List<Object> gridTypesData = new ArrayList<>();
        List<Fragment> gridTypesFragments = new ArrayList<>();

        for(SamuraiGridType gridType: SamuraiGridType.values()) {
            gridTypesData.add(gridType);
            gridTypesFragments.add(new SudokuTypeFragment(gridType.name().replace("_", " ")));
        }

        mGameType = new ButtonViewPager(mActivity.getActivity(),
                R.id.btn_prev_sudoku_mode,
                R.id.btn_next_sudoku_mode,
                R.id.viewPager_sudoku_mode);
        mGameType.setFragmentType(FragmentType.SUDOKU_TYPE_FRAGMENT);
        mGameType.setData(mActivity.getMainActivity().getSupportFragmentManager(),
                          mActivity.getMainActivity().getLifecycle(),
                          sudokuTypesData,
                          sudokuTypesFragments);

        mGameMode = new ButtonViewPager(mActivity.getActivity(),
                R.id.btn_prev_game_mode,
                R.id.btn_next_game_mode,
                R.id.viewPager_game_mode);
        mGameMode.setFragmentType(FragmentType.GAME_MODE_FRAGMENT);
        mGameMode.setData(mActivity.getMainActivity().getSupportFragmentManager(),
                          mActivity.getMainActivity().getLifecycle(),
                          gameModeData,
                          gameModeFragments);

        mGridMode = new ButtonViewPager(mActivity.getActivity(),
                R.id.btn_prev_sudoku_grid,
                R.id.btn_next_sudoku_grid,
                R.id.viewPager_sudoku_grid);
        mGridMode.setFragmentType(FragmentType.SUDOKU_TYPE_FRAGMENT);
        mGridMode.setData(mActivity.getMainActivity().getSupportFragmentManager(),
                          mActivity.getMainActivity().getLifecycle(),
                          gridTypesData,
                          gridTypesFragments);

        mGridMode.setOnItemChangedAction(this::onViewPagerChanged);
        mGameMode.setOnItemChangedAction(this::onViewPagerChanged);
        mGameType.setOnItemChangedAction(this::onViewPagerChanged);
    }

    private void setViewPagers() {
        if(!isFirstRun) {
            int s_type = mActivityData.getLastSelectedSudoku();
            int s_level = mActivityData.getLastSelectedLevel();
            int s_grid = mActivityData.getLastSelectedGrid();

            mGameType.setCurrentItem(s_type, false);
            mGameMode.setCurrentItem(s_level, false);
            mGridMode.setCurrentItem(s_grid, false);
        }
        else {
            mGameType.setCurrentItem(0, false);
            mGameMode.setCurrentItem(0, false);
            mGridMode.setCurrentItem(0, false);
        }
    }

    //On Click Methods
    private void onClickNewGame() {}
    private void onClickResumeGame() {}
    private void onClickSettings() {}
    private void onClickShop() {}
    private void onClickStatistics() {}
    private void onClickAbout() {}
    private void onClickDayNight() {}

    //Events
    private void onViewPagerChanged() {
        Log.e(TAG, "View Pager Changed");
    }

    //Overrides
    private int curTheme = -1;
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

        if(!isDarkMode) {
            mMainActivity.getWindow().getDecorView().getRootView().setBackgroundColor(getTheme().getBackgroundColor());
            mMainActivity.getWindow().setStatusBarColor(getTheme().getBackgroundColor());

            if(getTheme().getBackgroundColor() == Color.WHITE) {
                ThemeUtil.setStatusBarColor(mMainActivity, getTheme().getBackgroundColor(), true);
            }
            else {
                ThemeUtil.setStatusBarColor(mMainActivity, getTheme().getBackgroundColor(), false);
            }
        }

        mBtn_NewGame.setTheme(getTheme());
        mBtn_ResumeGame.setTheme(getTheme());

        //Update View Pagers Themes
        mGridMode.setTheme(getTheme());
        mGameMode.setTheme(getTheme());
        mGameType.setTheme(getTheme());

        mBtn_Settings.setSupportImageTintList(ColorStateList.valueOf(getTheme().getPrimaryColor()));
        mBtn_Shop.setSupportImageTintList(ColorStateList.valueOf(getTheme().getPrimaryColor()));
        mBtn_Statistics.setSupportImageTintList(ColorStateList.valueOf(getTheme().getPrimaryColor()));
        mBtn_About.setSupportImageTintList(ColorStateList.valueOf(getTheme().getPrimaryColor()));
        mBtn_DayNight.setSupportImageTintList(ColorStateList.valueOf(getTheme().getPrimaryColor()));
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

    public View findViewById(int id) {
        return mActivity.findViewById(id);
    }
}
