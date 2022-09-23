package com.realgear.sudoku_thebestone.utils;

import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.SkuDetails;

import java.util.List;

public interface ActionBillingClient {
    public void updateUI(BillingResult billingResult, List<SkuDetails> list);
}
