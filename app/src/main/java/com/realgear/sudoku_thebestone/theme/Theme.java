package com.realgear.sudoku_thebestone.theme;

import android.content.Context;

import androidx.core.content.ContextCompat;

public final class Theme {
    private int mPrimaryColor;
    private int mSecondaryColor;
    private int mSecondaryColor2;
    private int mBackgroundColor;

    private int mDefault;

    private int mOrdinal;

    public Theme(Context ctx, int PrimaryColor,
                 int SecondaryColor,
                 int SecondaryColor2,
                 int BackgroundColor) {
        this.mPrimaryColor      = ContextCompat.getColor(ctx, PrimaryColor);
        this.mSecondaryColor    = ContextCompat.getColor(ctx, SecondaryColor);;
        this.mSecondaryColor2   = ContextCompat.getColor(ctx, SecondaryColor2);
        this.mBackgroundColor   = ContextCompat.getColor(ctx, BackgroundColor);
    }

    public Theme() {}

    public void setOrdinal(int ordinal) {
        this.mOrdinal = ordinal;
    }

    public int getOrdinal() { return mOrdinal; }

    public int getPrimaryColor() { return mPrimaryColor; }
    public int getSecondaryColor() { return mSecondaryColor; }
    public int getSecondaryColor2() { return mSecondaryColor2; }
    public int getBackgroundColor() { return mBackgroundColor; }

    public void setPrimaryColor(int color) {
        this.mPrimaryColor = color;
    }
    public void setSecondaryColor(int color) {
        this.mSecondaryColor = color;
    }
    public void setSecondaryColor2(int color) {
        this.mSecondaryColor2 = color;
    }
    public void setBackgroundColor(int color) {
        this.mBackgroundColor = color;
    }
}
