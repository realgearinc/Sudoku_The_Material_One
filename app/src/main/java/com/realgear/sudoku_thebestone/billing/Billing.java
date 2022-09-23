package com.realgear.sudoku_thebestone.billing;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Billing {

    private final String REMOVE_ADS = "remove_ads";

    private PurchasesUpdatedListener purchasesUpdatedListener = new PurchasesUpdatedListener() {
        @Override
        public void onPurchasesUpdated(@NonNull @NotNull BillingResult billingResult, @Nullable @org.jetbrains.annotations.Nullable List<Purchase> list) {
            if(billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && list != null) {
                Log.e("Billing", "Purchase is success");
            }
            else if(billingResult.getResponseCode() == BillingClient.BillingResponseCode.ITEM_UNAVAILABLE) {
                Log.e("Billing", "Item is not available");
            }
            else {
                Log.e("Billing", billingResult.getDebugMessage());
            }
        }
    };

    private BillingClient billingClient;

    private boolean isReady = false;

    private Activity mActivity;

    public Billing(Activity activity) {

        mActivity = activity;

        billingClient = BillingClient.newBuilder(activity)
                .setListener(purchasesUpdatedListener)
                .enablePendingPurchases()
                .build();

        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingServiceDisconnected() {
                Log.e("Billing", "Client Disconnected");
            }

            @Override
            public void onBillingSetupFinished(@NonNull @NotNull BillingResult billingResult) {
                if(billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    Log.e("Billing", "Client Is Ready");
                    isReady = true;
                    startPurchase();
                }
            }
        });


    }

    private void startPurchase() {
        if(isReady) {
            List<String> skuList = new ArrayList<>();
            skuList.add("remove_ads");

            SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
            params.setSkusList(skuList).setType(BillingClient.SkuType.INAPP);
            billingClient.querySkuDetailsAsync(params.build(),
                    (billingResult1, list) -> {

                        if(billingResult1.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                            Log.e("Billing", "Purchase Is Ready");

                            if(list != null && list.size() != 0) {
                                BillingFlowParams flowParams = BillingFlowParams.newBuilder()
                                        .setSkuDetails(list.get(0))
                                        .build();

                                billingClient.launchBillingFlow(mActivity, flowParams);
                            }
                            else {
                                Log.e("Billing", "List Is Empty");
                            }
                        }
                        else {
                            Log.e("Billing", "Response Code : " + billingResult1.getResponseCode());
                        }



                        for(SkuDetails sku: list) {
                            Log.e("Billing", "Title : " + sku.getTitle());
                            Log.e("Billing", "Price : " + sku.getPrice());
                        }
                    });
        }
    }
}
