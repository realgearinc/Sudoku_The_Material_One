package com.realgear.sudoku_thebestone.example;

import android.os.Bundle;

import com.realgear.sudoku_thebestone.MainActivity;
import com.realgear.sudoku_thebestone.activities.ActivityEnums;
import com.realgear.sudoku_thebestone.ads.AppOpenManager;
import com.realgear.sudoku_thebestone.ads.InterstitialAdHandler;
import com.realgear.sudoku_thebestone.core.ActivityManager;

public class AdThread {

    private final ActivityManager   mActivityManager;

    private InterstitialAdHandler   mAdHandler;
    private AppOpenManager          mAdOnOpen;

    public AdThread(MainActivity activity) {
        this.mAdHandler = new InterstitialAdHandler(activity);
        this.mAdOnOpen  = new AppOpenManager(activity.getApplication());

        this.mActivityManager = new ActivityManager(activity);
    }

    public void runActivity(ActivityEnums activity, Bundle bundle) {
        try {
            this.mAdHandler.showAd(() -> mActivityManager.runActivity(activity, bundle));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
