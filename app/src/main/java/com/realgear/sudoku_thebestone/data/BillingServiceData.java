package com.realgear.sudoku_thebestone.data;

import android.content.Context;

public class BillingServiceData extends DataPref {

    private final String KEY_REMOVED_ADS = "REMOVED_ADS";

    public BillingServiceData(Context context) {
        super(context, BillingServiceData.class.getSimpleName());
    }

    public void setRemovedAds(boolean value) {
        putBoolean(KEY_REMOVED_ADS, value);
    }

    public boolean removedAds() {
        return getBoolean(KEY_REMOVED_ADS);
    }

}
