package com.realgear.sudoku_thebestone.data;

import android.content.Context;

public class SettingsData extends DataPref {

    private final String KEY_HIGHLIGHT_GOALS = "HIGHLIGHT_GOALS";
    private final String KEY_HIGHLIGHT_SAME_DIGITS = "HIGHLIGHT_SAME_DIGITS";
    private final String KEY_HIGHLIGHT_REMAINING = "HIGHLIGHT_REMAINING";
    private final String KEY_SHOW_TIME = "SHOW_TIME";
    private final String KEY_SHOW_HINT = "SHOW_HINT";

    private final String KEY_FIRST_RUN = "FIRST_RUN";


    public SettingsData(Context context) {
        super(context, SettingsData.class.getSimpleName());
    }

    public boolean isFirstRun() {
        return !getBoolean(KEY_FIRST_RUN);
    }

    public void setFirstRun() {
        putBoolean(KEY_FIRST_RUN, true);
    }

    public boolean getHighlightGoals() {
        return getBoolean(KEY_HIGHLIGHT_GOALS);
    }
    public boolean getHighlightSameDigits() {
        return getBoolean(KEY_HIGHLIGHT_SAME_DIGITS);
    }
    public boolean getHighlightRemaining() {
        return getBoolean(KEY_HIGHLIGHT_REMAINING);
    }
    public boolean getShowTime() {
        return getBoolean(KEY_SHOW_TIME);
    }
    public boolean getShowHint() {
        return getBoolean(KEY_SHOW_HINT);
    }

    public void setHighlightGoals(boolean value) {
        putBoolean(KEY_HIGHLIGHT_GOALS, value);
    }

    public void setHighlightSameDigits(boolean value) {
        putBoolean(KEY_HIGHLIGHT_SAME_DIGITS, value);
    }

    public void setHighlightRemaining(boolean value) {
        putBoolean(KEY_HIGHLIGHT_REMAINING, value);
    }

    public void setShowTime(boolean value) {
        putBoolean(KEY_SHOW_TIME, value);
    }

    public void setShowHint(boolean value) {
        putBoolean(KEY_SHOW_HINT, value);
    }
}
