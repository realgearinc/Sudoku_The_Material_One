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

public class SudokuTypeFragment extends Fragment {
    private Theme mTheme;

    public String LevelType;

    public SudokuTypeFragment(){}

    public SudokuTypeFragment(String levelType)
    {
        LevelType = levelType;
    }

    private TextView levelTypeView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.layout_sudoku_type, container, false);

        levelTypeView = rootView.findViewById(R.id.textView_sudokuType);
        levelTypeView.setText(LevelType);

        if(mTheme != null) {
            levelTypeView.setTextColor(mTheme.getPrimaryColor());
        }

        return rootView;
    }

    public void setTheme(Theme theme) {
        this.mTheme = theme;

        if(levelTypeView != null) {
            levelTypeView.setTextColor(theme.getPrimaryColor());
        }
    }
}
