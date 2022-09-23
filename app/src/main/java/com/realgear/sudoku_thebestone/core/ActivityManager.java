package com.realgear.sudoku_thebestone.core;

import android.content.Intent;
import android.os.Bundle;

import com.realgear.sudoku_thebestone.MainActivity;
import com.realgear.sudoku_thebestone.activities.About;
import com.realgear.sudoku_thebestone.activities.ActivityEnums;
import com.realgear.sudoku_thebestone.activities.Gameplay;
import com.realgear.sudoku_thebestone.activities.PlayStore;
import com.realgear.sudoku_thebestone.activities.Settings;
import com.realgear.sudoku_thebestone.activities.Statistics;

import java.util.TreeMap;

public class ActivityManager {
    private final String TAG = ActivityManager.class.getSimpleName();

    private final MainActivity mActivity;

    private TreeMap<ActivityEnums, Class> mActivities;

    public ActivityManager(MainActivity activity) {
        this.mActivity = activity;
        this.mActivities = getActivities();
    }

    private TreeMap<ActivityEnums, Class> getActivities() {
        TreeMap<ActivityEnums, Class> result = new TreeMap<>();

        result.put(ActivityEnums.GAMEPLAY_ACTIVITY, Gameplay.class);
        result.put(ActivityEnums.SETTINGS_ACTIVITY, Settings.class);
        result.put(ActivityEnums.PLAYSTORE_ACTIVITY, PlayStore.class);
        result.put(ActivityEnums.STATISTICS_ACTIVITY, Statistics.class);
        result.put(ActivityEnums.ABOUT_ACTIVITY, About.class);

        return result;
    }

    public void runActivity(ActivityEnums activity, Bundle bundle) {
        if(!mActivities.containsKey(activity))
            return;

        Intent intent = new Intent(mActivity, mActivities.get(activity));

        if(bundle != null) {
            intent.putExtras(bundle);
        }

        mActivity.startActivity(intent);
    }
}
