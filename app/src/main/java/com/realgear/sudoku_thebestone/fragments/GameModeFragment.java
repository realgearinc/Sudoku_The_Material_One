package com.realgear.sudoku_thebestone.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.realgear.sudoku_thebestone.R;
import com.realgear.sudoku_thebestone.theme.Theme;

public class GameModeFragment extends Fragment {

    private Theme mTheme;

    public String LevelType;
    public int CurrentLevel;

    public GameModeFragment() {

    }

    public GameModeFragment(String levelType, int currentLevel)
    {
        LevelType = levelType;
        CurrentLevel = currentLevel;
    }

    private TextView levelTypeView;
    private TextView currentLevelView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.layout_level_mode, container, false);

        levelTypeView = rootView.findViewById(R.id.textView_levelType);
        levelTypeView.setText(LevelType);

        currentLevelView = rootView.findViewById(R.id.textView_currentLevel);
        currentLevelView.setText("Level : " + this.CurrentLevel);

        if(mTheme != null) {
            levelTypeView.setTextColor(mTheme.getPrimaryColor());
            currentLevelView.setTextColor(mTheme.getPrimaryColor());
        }

        return rootView;
    }

    public void setLevel(int level) {
        this.CurrentLevel = level;

        if(currentLevelView != null) {
            currentLevelView.setText("Level : " + this.CurrentLevel);
        }
    }

    public void setTheme(Theme theme) {
        this.mTheme = theme;

        if(levelTypeView != null) {
            levelTypeView.setTextColor(theme.getPrimaryColor());
        }

        if(currentLevelView != null) {
            currentLevelView.setTextColor(theme.getPrimaryColor());
        }
    }
}
