package com.realgear.sudoku_thebestone.example;

import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.realgear.sudoku_thebestone.MainActivity;
import com.realgear.sudoku_thebestone.R;
import com.realgear.sudoku_thebestone.activities.ActivityEnums;
import com.realgear.sudoku_thebestone.core.ActivityDataManager;
import com.realgear.sudoku_thebestone.core.ActivityManager;
import com.realgear.sudoku_thebestone.core.GameType;
import com.realgear.sudoku_thebestone.core.SudokuTypes;
import com.realgear.sudoku_thebestone.data.SettingsData;
import com.realgear.sudoku_thebestone.data.sudoku.SudokuData;
import com.realgear.sudoku_thebestone.theme.Themes;
import com.realgear.sudoku_thebestone.utils.SamuraiGridType;
import com.realgear.sudoku_thebestone.view.MainActivityView;
import com.realgear.sudoku_thebestone.view.ThemeSelectorView;

public class MainActivityExample {
    public final static String TAG = MainActivityExample.class.getSimpleName();

    private final MainActivity    mActivity;

    private final ThemeSelectorView     mThemeSelectorView;
    private final MainActivityView      mActivityView;
    private final ActivityDataManager   mDataManager;

    private final AdThread         mAdThread;

    public MainActivityExample(MainActivity activity) {
        this.mActivity = activity;
        this.mAdThread = new AdThread(mActivity);

        this.mDataManager = new ActivityDataManager(mActivity);

        this.mActivityView      = new MainActivityView(mActivity);
        this.mThemeSelectorView = new ThemeSelectorView(mActivity, mActivity.findViewById(R.id.include), mActivityView);
    }

    public void init() {
        if(mActivityView.isFirstRun) {
            this.mThemeSelectorView.selectTheme(Themes.WHITE_STEEL_BLUE);
        }
        else {
            this.mThemeSelectorView.selectTheme(mActivityView.isFirstTheme);
        }

        startFadeInAnim();
    }

    private void startFadeInAnim() {
        RelativeLayout fadeOut = mActivity.findViewById(R.id.fade_in_overlay);
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

    public void setDarkMode(boolean isDarkMode) {
        mActivityView.setDayNightTheme(isDarkMode);
    }

    public void runActivity(ActivityEnums activity, Bundle bundle) {
        this.mAdThread.runActivity(activity, bundle);
    }

    private void initSettingsData() {
        SettingsData data = new SettingsData(mActivity);

        if(data.isFirstRun()) {
            data.setHighlightSameDigits(true);
            data.setHighlightRemaining(true);
            data.setHighlightGoals(true);
            data.setShowTime(true);
            data.setShowHint(true);

            data.setFirstRun();
        }
    }

    public ThemeSelectorView getThemeSelectorView() {
        return this.mThemeSelectorView;
    }

    public SudokuData getSudokuData(SamuraiGridType gridType, GameType level, SudokuTypes type) {
        return this.mDataManager.getSudokuData(gridType, level, type);
    }

    public Context getContext() {
        return this.mActivity;
    }

    public View findViewById(int id) {

    }

    public MainActivity getMainActivity() {
        return this.mActivity;
    }

    public Activity getActivity() {
        return this.mActivity;
    }
}
