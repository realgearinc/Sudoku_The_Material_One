package com.realgear.sudoku_thebestone.theme;

import android.content.Context;

import com.realgear.sudoku_thebestone.R;

import java.util.TreeMap;

public class ThemeManager {
    public final TreeMap<Themes, Theme> mThemes = new TreeMap<>();

    private Context ctx;

    public ThemeManager(Context ctx) {
        this.ctx = ctx;

        mThemes.put(Themes.WHITE_STEEL_BLUE, getTheme1());
        mThemes.put(Themes.WHITE_INDIAN_RED, getTheme2());
        mThemes.put(Themes.LIGHT_BLUE_WHITE, getTheme3());
        mThemes.put(Themes.FOREST_GREEN_WHITE, getTheme4());
        mThemes.put(Themes.GREY_WHITE, getTheme5());

        setOrdinal(Themes.WHITE_STEEL_BLUE);
        setOrdinal(Themes.WHITE_INDIAN_RED);
        setOrdinal(Themes.LIGHT_BLUE_WHITE);
        setOrdinal(Themes.FOREST_GREEN_WHITE);
        setOrdinal(Themes.GREY_WHITE);
    }

    public void setOrdinal(Themes theme) {
        mThemes.get(theme).setOrdinal(theme.ordinal());
    }

    public Theme getTheme(Themes theme) {
        if(mThemes.containsKey(theme)) {
            return mThemes.get(theme);
        } else {
            return mThemes.get(Themes.WHITE_STEEL_BLUE);
        }
    }

    private Theme getTheme1() {

        return new Theme(ctx,R.color.mat_steel_blue,
                           R.color.mat_grey,
                           R.color.mat_light_grey,
                           R.color.white);
    }

    private Theme getTheme2() {
        return new Theme(ctx,R.color.mat_indian_red,
                            R.color.mat_grey,
                            R.color.mat_light_grey,
                            R.color.white);
    }

    private Theme getTheme3() {
        return new Theme(ctx, R.color.mat_deep_sky_blue,
                R.color.mat_grey,
                R.color.mat_light_grey,
                R.color.white);
    }

    private Theme getTheme4() {
        return new Theme(ctx, R.color.mat_forest_green,
                R.color.mat_grey,
                R.color.mat_light_grey,
                R.color.black_green);
    }

    private Theme getTheme5() {
        return new Theme(ctx, R.color.mat_grey,
                R.color.mat_grey,
                R.color.mat_light_grey,
                R.color.black_blue);
    }
}
