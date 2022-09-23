package com.realgear.sudoku_thebestone.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Configuration;
import android.os.Bundle;

import com.realgear.sudoku_thebestone.R;
import com.realgear.sudoku_thebestone.theme.ThemeManager;
import com.realgear.sudoku_thebestone.theme.Themes;
import com.realgear.sudoku_thebestone.utils.Keys;
import com.realgear.sudoku_thebestone.view.StatisticsView;

import org.jetbrains.annotations.NotNull;

public class Statistics extends AppCompatActivity {

    private ThemeManager mThemeManager;
    private StatisticsView mActivityView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        Bundle bundle = getIntent().getExtras();
        int themeOrdinal = bundle.getInt(Keys.GAME_THEME);

        mActivityView = new StatisticsView(this);
        mThemeManager = new ThemeManager(this);

        mActivityView.setTheme(mThemeManager.getTheme(Themes.values()[themeOrdinal]));

        setDarkMode();
    }

    private void setDarkMode() {
        int NIGHT_MODE_FLAGS = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if (NIGHT_MODE_FLAGS == Configuration.UI_MODE_NIGHT_YES) {
            mActivityView.setDayNightTheme(true);
        } else if (NIGHT_MODE_FLAGS == Configuration.UI_MODE_NIGHT_NO) {
            mActivityView.setDayNightTheme(false);
        }
    }

    @Override
    public void onConfigurationChanged(@NonNull @NotNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        int NIGHT_MODE_FLAGS = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if (NIGHT_MODE_FLAGS == Configuration.UI_MODE_NIGHT_YES) {
            mActivityView.setDayNightTheme(true);
        } else if (NIGHT_MODE_FLAGS == Configuration.UI_MODE_NIGHT_NO) {
            mActivityView.setDayNightTheme(false);
        }
    }
}