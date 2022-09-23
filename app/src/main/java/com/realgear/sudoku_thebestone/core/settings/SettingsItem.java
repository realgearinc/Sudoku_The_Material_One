package com.realgear.sudoku_thebestone.core.settings;

import com.realgear.sudoku_thebestone.utils.Action;

public class SettingsItem {
    private final String mTitle;
    private final String mSubtitle;
    private final SettingsKey mKey;

    private boolean isEnabled;
    private Action mAction;

    public SettingsItem(String title, String subtitle, SettingsKey key) {
        this.mTitle     = title;
        this.mSubtitle  = subtitle;
        this.mKey       = key;
    }

    public void setAction(Action action) {
        this.mAction = action;
    }

    public String getTitle() {
        return this.mTitle;
    }
    public String getSubtitle() {
        return this.mSubtitle;
    }

    public boolean isEnabled() {
        return this.isEnabled;
    }

    public void invoke(boolean b) {
        isEnabled = b;
        //Log.e("SettingsItem", "Invoke : " + mKey.toString() + " : " + b);
        try {
            mAction.call();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setEnabled(boolean b) {
        this.isEnabled = b;
    }
}
