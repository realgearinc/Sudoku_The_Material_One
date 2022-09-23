package com.realgear.sudoku_thebestone.view.sudokuparams;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.realgear.sudoku_thebestone.theme.Theme;
import com.realgear.sudoku_thebestone.theme.Themeable;


public class ParamView extends Themeable {
    private Theme mTheme;
    private final FloatingActionButton mButton;
    private final TextView mValueTextView;
    private final TextView mCountTextView;
    private final FrameLayout mFrameLayout;

    private final int mValue;

    private boolean isSelected = false;

    public ParamView(FloatingActionButton button,TextView valueTextView, TextView countTextView, FrameLayout frameLayout, int value, int count)
    {
        this.mButton = button;
        this.mValueTextView = valueTextView;
        this.mCountTextView = countTextView;
        this.mFrameLayout = frameLayout;
        this.mValue = value;

        if(mButton != null) {
            mButton.setRippleColor(ColorStateList.valueOf(Color.argb(0, 0,0, 0)));
        }

        if(mCountTextView != null) {
            mCountTextView.setText(String.valueOf(count));
        }
    }

    public void setCount(int count) {
        mCountTextView.setText(String.valueOf(count));
    }

    public void setIsSelected(boolean isSelected) {
        if(this.isSelected == isSelected)
            return;

        this.isSelected = isSelected;
        updateTheme();
    }

    public FloatingActionButton getButton() {
        return this.mButton;
    }

    public FrameLayout getFrame() { return this.mFrameLayout; }

    public int getValue() { return this.mValue; }

    public boolean isSelected() {
        return this.isSelected;
    }

    @Override
    public void updateTheme() {
        mTheme = getTheme();

        mButton.setSupportBackgroundTintList(
                ColorStateList.valueOf(mTheme.getPrimaryColor()));

        if(isSelected) {
            mFrameLayout.setBackgroundTintList(
                    ColorStateList.valueOf(mTheme.getPrimaryColor()));

                mValueTextView.setTextColor(Color.WHITE);
                mCountTextView.setTextColor(Color.WHITE);

        }
        else {
            mFrameLayout.setBackgroundTintList(
                    ColorStateList.valueOf(
                            Color.argb(0, 0, 0, 0)));

            mValueTextView.setTextColor(mTheme.getPrimaryColor());
            mCountTextView.setTextColor(mTheme.getPrimaryColor());


        }


    }

    public void setCountVisibility(int visibility) {
        mCountTextView.setVisibility(visibility);
    }
}
