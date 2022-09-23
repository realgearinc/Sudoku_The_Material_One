package com.realgear.sudoku_thebestone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.realgear.sudoku_thebestone.activities.ActivityEnums;
import com.realgear.sudoku_thebestone.ads.InterstitialAdHandler;
import com.realgear.sudoku_thebestone.core.ActivityDataManager;
import com.realgear.sudoku_thebestone.core.ActivityManager;
import com.realgear.sudoku_thebestone.core.GameType;
import com.realgear.sudoku_thebestone.core.SudokuTypes;
import com.realgear.sudoku_thebestone.data.SettingsData;
import com.realgear.sudoku_thebestone.data.sudoku.SudokuData;
import com.realgear.sudoku_thebestone.example.MainActivityExample;
import com.realgear.sudoku_thebestone.theme.Themes;
import com.realgear.sudoku_thebestone.utils.SamuraiGridType;
import com.realgear.sudoku_thebestone.view.MainActivityView;
import com.realgear.sudoku_thebestone.view.ThemeSelectorView;

import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity {

    public final String TAG = "MainActivity";

    private ActivityManager      mActivityManager;
    private MainActivityView     mActivityView;
    private ThemeSelectorView    mThemeSelectorView;
    private ActivityDataManager  mDataManager;


    //Ad Manager
    private InterstitialAdHandler mAdHandler;
    //private AppOpenManager mAdOnOpen;


    private MainActivityExample mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        boolean isReturn = true;

        //Init UI
        setContentView(R.layout.activity_main);

        if(isReturn) {
            mActivity = new MainActivityExample(this);
            mActivity.init();
            return;
        }

        /*

        //Init Ad & Data Managers
        new AppOpenManager(this.getApplication());
        mAdHandler = new InterstitialAdHandler(this);

        //Init Settings
        initSettings();

        //Handle Data
        mDataManager = new ActivityDataManager(this);

        //Init UI
        setContentView(R.layout.activity_main);

        startGame();

        LocalDateTime localDateTime = LocalDateTime.now();*/
    }

    private void initSettings() {
        SettingsData settingsData = new SettingsData(this);

        if(settingsData.isFirstRun()) {
            settingsData.setFirstRun();
            settingsData.setHighlightGoals(true);
            settingsData.setHighlightRemaining(true);
            settingsData.setHighlightSameDigits(true);
            settingsData.setShowHint(true);
            settingsData.setShowTime(true);
        }
    }

    public SudokuData getSudokuData(SamuraiGridType gridType, GameType level, SudokuTypes type) {
        return this.mActivity.getSudokuData(gridType, level, type);
    }

    public ThemeSelectorView getThemeSelectorView() {
        return this.mActivity.getThemeSelectorView();
    }

    public void startGame() {
        mActivityView       = new MainActivityView(this);

        mThemeSelectorView  = new ThemeSelectorView(getApplicationContext(), findViewById(R.id.include), mActivityView);
        mActivityManager    = new ActivityManager(this);

        if(mActivityView.isFirstRun) {
            mThemeSelectorView.selectTheme(Themes.WHITE_STEEL_BLUE);
        }

        mThemeSelectorView.selectTheme(mActivityView.isFirstTheme);

        setDarkMode();

        RelativeLayout fadeOut = findViewById(R.id.fade_in_overlay);
        ValueAnimator newAnimator = ValueAnimator.ofObject(new TypeEvaluator<Float>() {
            @Override
            public Float evaluate(float fraction, Float startValue, Float endValue) {
                return (startValue + (endValue - startValue) * fraction);
            }
        }, 1.0f, 0.0f);

        newAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float value = (float)valueAnimator.getAnimatedValue();
                fadeOut.setAlpha(value);
                if(value <= 0.02f) {
                    fadeOut.setVisibility(View.GONE);
                }

            }
        });
        newAnimator.setDuration(350);
        newAnimator.start();
    }

    private void setDarkMode() {
        int NIGHT_MODE_FLAGS = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;

        if (NIGHT_MODE_FLAGS == Configuration.UI_MODE_NIGHT_YES) {
            mActivityView.setDayNightTheme(true);
        }
        else if (NIGHT_MODE_FLAGS == Configuration.UI_MODE_NIGHT_NO) {
            mActivityView.setDayNightTheme(false);
        }
    }

    public void runActivity(ActivityEnums activity, Bundle bundle) {
        this.mActivity.runActivity(activity, bundle);
    }

    @Override
    public void onConfigurationChanged(@NonNull @NotNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        int NIGHT_MODE_FLAGS = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;

        if (NIGHT_MODE_FLAGS == Configuration.UI_MODE_NIGHT_YES) {
            mActivity.setDarkMode(true);
        }
        else if (NIGHT_MODE_FLAGS == Configuration.UI_MODE_NIGHT_NO) {
            mActivity.setDarkMode(false);
        }
    }
}