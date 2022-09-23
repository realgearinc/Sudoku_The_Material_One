package com.realgear.sudoku_thebestone.billing;

import android.app.Activity;

import androidx.annotation.NonNull;

import com.android.billingclient.api.AcknowledgePurchaseParams;
import com.android.billingclient.api.AcknowledgePurchaseResponseListener;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesResponseListener;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class BillingService {
    public final static String REMOVE_ADS = "remove_ads";

    private final PurchasesUpdatedListener mPurchaseUpdateListener;
    private final BillingClientStateListener mBillingStateListener;
    private final SkuDetailsResponseListener mSkuDetailsListener;
    private final AcknowledgePurchaseResponseListener mAcknowledgeListener;
    private final PurchasesResponseListener mPurchaseResponseListener;

    private BillingClient mBillingClient;
    private boolean isReady = false;

    private final Activity mActivity;

    public BillingService(Activity activity,
                          PurchasesUpdatedListener listener,
                          BillingClientStateListener stateListener,
                          SkuDetailsResponseListener skuListener,
                          AcknowledgePurchaseResponseListener acknowledgeResponseListener,
                          PurchasesResponseListener purchasesResponseListener) {
        this.mActivity = activity;

        this.mPurchaseUpdateListener    = listener;
        this.mBillingStateListener      = stateListener;
        this.mSkuDetailsListener        = skuListener;
        this.mAcknowledgeListener       = acknowledgeResponseListener;
        this.mPurchaseResponseListener  = purchasesResponseListener;

        init();
    }

    private void init() {
        mBillingClient = BillingClient.newBuilder(mActivity)
                .setListener(mPurchaseUpdateListener)
                .enablePendingPurchases()
                .build();
    }

    public void startConnection() {
        mBillingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingServiceDisconnected() {
                mBillingStateListener.onBillingServiceDisconnected();
            }

            @Override
            public void onBillingSetupFinished(@NonNull @NotNull BillingResult billingResult) {

                if(billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    isReady = true;
                }
                else {
                    isReady = false;
                }

                mBillingStateListener.onBillingSetupFinished(billingResult);
                setSkuList();
            }
        });
    }

    public void setSkuList() {
        if(isReady) {
            List<String> skuList = new ArrayList<>();
            skuList.add(REMOVE_ADS);

            SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
            params.setSkusList(skuList).setType(BillingClient.SkuType.INAPP);
            mBillingClient.querySkuDetailsAsync(params.build(), mSkuDetailsListener);
        }
    }

    public void launchBillingFlow(SkuDetails skuDetails) {
        if(isReady && skuDetails != null) {
            BillingFlowParams flowParams = BillingFlowParams.newBuilder()
                    .setSkuDetails(skuDetails)
                    .build();

            mBillingClient.launchBillingFlow(mActivity, flowParams);
        }
    }

    public void handlePurchase(Purchase purchase) {
        if(!purchase.isAcknowledged()) {
            AcknowledgePurchaseParams acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
                    .setPurchaseToken(purchase.getPurchaseToken())
                    .build();

            mBillingClient.acknowledgePurchase(acknowledgePurchaseParams, mAcknowledgeListener);
        }
    }

    public void queryPurchases() {
        mBillingClient.queryPurchasesAsync(BillingClient.SkuType.INAPP, mPurchaseResponseListener);

        /*mBillingClient.queryPurchasesAsync(BillingClient.SkuType.INAPP, (billingResult, list) -> {
            if(billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                if(list != null && list.size() != 0) {
                    Log.e("Billing Service", "Query Purchases \n");
                    for(Purchase purchase: list) {
                        //Enable All Features
                        Log.e("Billing Service", "Is Purchased ? " + purchase.getPurchaseState());
                    }
                }
            }
        });*/
    }

    public boolean isReady() {
        return this.isReady;
    }
}
