package com.realgear.sudoku_thebestone.view;

import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.realgear.sudoku_thebestone.R;
import com.realgear.sudoku_thebestone.activities.Statistics;
import com.realgear.sudoku_thebestone.adapters.LevelDataSource;
import com.realgear.sudoku_thebestone.adapters.StateFragmentAdapter;
import com.realgear.sudoku_thebestone.core.GameType;
import com.realgear.sudoku_thebestone.core.Level;
import com.realgear.sudoku_thebestone.core.SudokuTypes;
import com.realgear.sudoku_thebestone.data.LevelData;
import com.realgear.sudoku_thebestone.fragments.StatisticFragment;
import com.realgear.sudoku_thebestone.fragments.SudokuTypeFragment;
import com.realgear.sudoku_thebestone.theme.Theme;
import com.realgear.sudoku_thebestone.theme.Themeable;
import com.realgear.sudoku_thebestone.theme.Themes;
import com.realgear.sudoku_thebestone.utils.SamuraiGridType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;

public class StatisticsView extends Themeable {
    private final String TAG = StatisticsView.class.getSimpleName();

    public FloatingActionButton mBtn_SudokuModePrev;
    public FloatingActionButton mBtn_SudokuModeNext;

    private final Statistics mActivity;
    private ViewPager2  mSudokuTypeViewPager;
    private ViewPager2  mStatisticViewPager;
    private TabLayout   mStatisticTabLayout;

    private List<SudokuTypes> mSudokuTypes;

    private TreeMap<SudokuTypes, TreeMap<GameType, List<Level>>> mLevels;
    private TreeMap<SudokuTypes, StatisticFragment> mFragments;

    private boolean isInit = false;
    private boolean isDarkTheme = false;

    private Theme mTheme;

    private RecyclerView mRecyclerView_Statistics;

    public StatisticsView(Statistics activity) {
        this.mActivity      = activity;
        this.mSudokuTypes   = new ArrayList<>();
        this.mLevels        = new TreeMap<>();
        this.mFragments     = new TreeMap<>();

        mSudokuTypeViewPager = (ViewPager2) findViewById(R.id.viewPager_sudoku_mode);
        mStatisticViewPager = (ViewPager2) findViewById(R.id.viewPager_statistics);
        mStatisticTabLayout = (TabLayout) findViewById(R.id.tabLayout_navigation);

        mBtn_SudokuModeNext = (FloatingActionButton) findViewById(R.id.btn_next_sudoku_mode);
        mBtn_SudokuModePrev = (FloatingActionButton) findViewById(R.id.btn_prev_sudoku_mode);

        initStatisticViewPager();
        initSudokuTypeViewPager();
        initLevels();

        FloatingActionButton backBtn = (FloatingActionButton)findViewById(R.id.back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mActivity.finish();
            }
        });
    }

    public void initLevels() {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                LevelData levelsData = new LevelData(mActivity);
                for(int i = 0; i < SudokuTypes.values().length; i++) {
                    SudokuTypes sudokuType = SudokuTypes.values()[i];
                    TreeMap<GameType, List<Level>> temp = new TreeMap<>();

                    for(int j = 0; j < GameType.values().length; j++) {
                        GameType gameType = GameType.values()[j];
                        List<Level> levels = new ArrayList<>();

                        if(sudokuType == SudokuTypes.SAMURAI) {
                            List<Level> temps = new ArrayList<>();
                            for(int x = 0; x < SamuraiGridType.values().length; x++) {
                                SamuraiGridType gridType = SamuraiGridType.values()[x];
                                List<Level> temp_ss = levelsData.getLevel(gameType, sudokuType, gridType);
                                temps.addAll(temp_ss);
                            }
                            levels = temps;
                        }
                        else {
                            levels = levelsData.getLevel(gameType, sudokuType, null);
                        }

                        temp.put(gameType, levels);
                    }

                    mLevels.put(sudokuType, temp);
                }

                for(StatisticFragment fragment : mFragments.values()) {
                    if(mLevels.containsKey(fragment.getSudokuType())) {
                        TreeMap<GameType, List<Level>> levels = mLevels.get(fragment.getSudokuType());
                        Log.e("StatisticView", fragment.getSudokuType().name());
                        for(GameType gameType : GameType.values()) {
                            if(levels.containsKey(gameType)) {
                                List<Level> temp = levels.get(gameType);
                                Log.e("StatisticView", gameType.name());
                                if(temp.size() > 0) {
                                    Log.e("StatisticView", "Added");
                                    fragment.addItem(gameType, new LevelDataSource(temp));
                                }
                            }
                        }
                    }
                }

                isInit = true;
                Log.e(TAG, "Init Success");
            }
        });
        t.run();
    }

    public void initStatisticViewPager() {
        if(mStatisticViewPager == null)
            return;

        StateFragmentAdapter adapter = new StateFragmentAdapter(mActivity.getSupportFragmentManager(), mActivity.getLifecycle());

        for(SudokuTypes i : SudokuTypes.values()) {
            mFragments.put(i, new StatisticFragment(i));
            adapter.addFragment(mFragments.get(i));
        }

        mStatisticViewPager.setAdapter(adapter);

        //mStatisticViewPager.setView
        mStatisticViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                mStatisticTabLayout.setScrollPosition(position, positionOffset, true, true);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                mStatisticTabLayout.selectTab(mStatisticTabLayout.getTabAt(position), true);

                //View scrollable = mFragments.get(SudokuTypes.values()[position]).getView();
                //mStatisticViewPager.set
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });

        mStatisticTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mStatisticViewPager.setCurrentItem(tab.getPosition(), true);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        for(int i = 0; i < mStatisticViewPager.getAdapter().getItemCount(); i++) {
            mStatisticTabLayout.getTabAt(i).setText(SudokuTypes.values()[i].name());
        }

        mStatisticViewPager.setOffscreenPageLimit(mStatisticViewPager.getChildCount());
        mStatisticViewPager.setDrawingCacheEnabled(true);
        mStatisticViewPager.setCurrentItem(0, false);
    }

    public void initSudokuTypeViewPager() {
        mSudokuTypes.add(SudokuTypes.STANDARD);
        mSudokuTypes.add(SudokuTypes.SAMURAI);
        mSudokuTypes.add(SudokuTypes.DAILY_CHALLENGE);

        if(mSudokuTypeViewPager == null)
            return;

        StateFragmentAdapter adapter = new StateFragmentAdapter(mActivity.getSupportFragmentManager(), mActivity.getLifecycle());
        for(int i = 0; i < mSudokuTypes.size(); i++) {
            adapter.addFragment(new SudokuTypeFragment(mSudokuTypes.get(i).toString().replace("_", " ")));
        }

        mSudokuTypeViewPager.setAdapter(adapter);
        mSudokuTypeViewPager.setOffscreenPageLimit(mSudokuTypes.size());


        mSudokuTypeViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                onSudokuTypeChanged();
            }
        });
    }

    public void onSudokuTypeChanged() {
        SudokuTypes sudokuType = SudokuTypes.values()[mSudokuTypeViewPager.getCurrentItem()];

        /*if(isInit) {
            TreeMap<GameType, List<Level>> levels = mLevels.get(sudokuType);
            for (GameType type : GameType.values()) {
                mFragments.get(type).setHistoryList(levels.get(type));
            }
        }*/
    }

    private ValueAnimator getFloatAnimator(float from, float to) {
        ValueAnimator floatAnimation = ValueAnimator.ofObject(new TypeEvaluator<Float>() {
            @Override
            public Float evaluate(float fraction, Float startValue, Float endValue) {
                return (startValue + (endValue - startValue) * fraction);
            }
        }, from, to);
        floatAnimation.setDuration(3000);
        return floatAnimation;
    }

    private View findViewById(int id) {
        return mActivity.findViewById(id);
    }

    private List<Level> getLevels(int sudokuTypeOrdinal, int gameTypeOrdinal) {
        SudokuTypes sudokuType  = SudokuTypes.values()[sudokuTypeOrdinal];
        GameType gameType       = GameType.values()[gameTypeOrdinal];

        //Load From Saved -> ToDo

        // --> End

        List<Level> levels = new ArrayList<>();

        /*for(int i = 0; i < getRandom(5, 20); i++) {
            String time     = getTime(getRandom(12, 108) * 9537);
            String date     = getRandom(15, 30) + " Sept 21";
            String endDat   = getRandom(1, 15) + " Sept 21";
            String rating   = getRandom(0, 9) + "." + getRandom(1, 9);
            levels.add(new Level(i, time, date, endDat, rating));
        }*/

        return levels;
    }

    public int getRandom(int min, int max) {
        List<Integer> result = new ArrayList<>();
        for(int i = min; i < max; i++) {
            result.add(i);
        }

        Collections.shuffle(result);
        return result.get(0);
    }

    public String getTime(int seconds)
    {
        int m = seconds / 60;
        int h = m / 60;
        int d = h / 24;


        int min     = seconds / 60;
        int hours   = min / 60;
        int days    = hours / 24;

        int curMin      = (min      - (hours    * 60));
        int curHours    = (hours    - (days     * 24));
        int curSeconds  = seconds   - ((curMin  * 60) + ((curHours * 60) * 60) + (((days * 24) * 60) * 60));

        String time = "";
        time += ((days     > 0) ? ""    + days      + "D " : "")    +
                ((curHours > 0) ? ""    + curHours  + "H " : "")    +
                ((curMin   > 0) ? ""    + curMin    + "M " : "")    + "" + curSeconds + "S";

        return time;
    }

    public void setDayNightTheme(boolean isDarkMode) {
        if(this.isDarkTheme == isDarkMode)
            return;

        if(isDarkMode) {
            if(getTheme().getOrdinal() == Themes.FOREST_GREEN_WHITE.ordinal() || getTheme().getOrdinal() == Themes.GREY_WHITE.ordinal()) {
                return;
            }

            int black_bg = ContextCompat.getColor(mActivity, R.color.black_grey);
            int white_bg = getTheme().getBackgroundColor();
            mActivity.getWindow().getDecorView().getRootView().setBackgroundColor(black_bg);
            setStatusBarColor(black_bg, false);

            this.isDarkTheme = true;
        }
        else {
            int black_bg = ContextCompat.getColor(mActivity, R.color.black_grey);
            int white_bg = Color.WHITE;

            mActivity.getWindow().getDecorView().getRootView().setBackgroundColor(white_bg);

            setStatusBarColor(white_bg, true);

            this.isDarkTheme = false;
            Log.e("MainActivityView", "Light Mode Set");
        }

        updateFragmentsTheme(getTheme());
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

    @Override
    public void setTheme(Theme theme) {
        this.mTheme = theme;
        super.setTheme(theme);
    }

    @Override
    public void updateTheme() {
        if(!isDarkTheme) {
            mActivity.getWindow().getDecorView().getRootView().setBackgroundColor(mTheme.getBackgroundColor());

            if(mTheme.getBackgroundColor() == Color.WHITE) {
                setStatusBarColor(mTheme.getBackgroundColor(), true);
            }
            else {
                setStatusBarColor(mTheme.getBackgroundColor(), false);
            }
        }

        //mBtn_SudokuModeNext.setSupportImageTintList(ColorStateList.valueOf(mTheme.getPrimaryColor()));
        //mBtn_SudokuModePrev.setSupportImageTintList(ColorStateList.valueOf(mTheme.getPrimaryColor()));

        mStatisticTabLayout.setSelectedTabIndicatorColor(mTheme.getPrimaryColor());
        mStatisticTabLayout.setTabRippleColor(ColorStateList.valueOf(mTheme.getPrimaryColor()));
        mStatisticTabLayout.setTabTextColors(mTheme.getSecondaryColor(), mTheme.getPrimaryColor());
    }

    public void updateFragmentsTheme(Theme theme) {
        StateFragmentAdapter adapter = (StateFragmentAdapter)mSudokuTypeViewPager.getAdapter();
        int count = adapter.getItemCount();
        for(int i = 0; i < count; i++) {
            SudokuTypeFragment fragment = (SudokuTypeFragment)adapter.getItem(i);
            fragment.setTheme(theme);
        }

        for(Fragment fragment: mFragments.values()) {
            StatisticFragment frag = (StatisticFragment)fragment;
            frag.updateTheme(theme);
        }
    }
}
