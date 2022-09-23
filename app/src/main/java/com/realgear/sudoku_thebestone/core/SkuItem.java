package com.realgear.sudoku_thebestone.core;

import android.view.View;

import com.android.billingclient.api.SkuDetails;

public class SkuItem {
    private final SkuDetails mSkuDetails;

    private boolean isPurchased = false;
    private View.OnClickListener mOnClickListener;

    public SkuItem(SkuDetails skuDetails, View.OnClickListener onClickListener) {
        this.mSkuDetails = skuDetails;
        this.mOnClickListener = onClickListener;
    }

    public void setPurchased(boolean value) {
        this.isPurchased = value;
    }

    public String getTitle() {
        return this.mSkuDetails.getTitle().replace("(Sudoku - The Best One)", "");
    }

    public String getDescription() {
        return this.mSkuDetails.getDescription();
    }

    public String getPrice() {
        return this.mSkuDetails.getPrice();
    }

    public SkuDetails getSkuDetails() {
        return this.mSkuDetails;
    }

    public View.OnClickListener getOnClickListener() {
        if(isPurchased) {
            return new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            };
        }
        else
            return this.mOnClickListener;
    }

    public boolean isPurchased() {
        return isPurchased;
    }
}
