package com.realgear.sudoku_thebestone.ads;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.billingclient.api.AcknowledgePurchaseResponseListener;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesResponseListener;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsResponseListener;
import com.realgear.sudoku_thebestone.billing.BillingService;
import com.realgear.sudoku_thebestone.data.BillingServiceData;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AdManager {
    private final static String TAG = AdManager.class.getSimpleName();

    private final BillingService        mBillingService;
    private final BillingServiceData    mBillingServiceData;
    private final Activity              mActivity;

    public AdManager(Activity activity) {
        this.mActivity = activity;

        this.mBillingServiceData = new BillingServiceData(mActivity);
        this.mBillingService = new BillingService(
                mActivity,
                getPurchaseUpdateListener(),
                getClientStateListener(),
                getSkuResponseListener(),
                getAcknowledgeResponseListener(),
                getPurchaseResponseListener()
        );

        connect();
    }

    private void connect() {
        if(mBillingService != null) {
            mBillingService.startConnection();
        }
    }

    private PurchasesUpdatedListener getPurchaseUpdateListener() {
        return new PurchasesUpdatedListener() {
            @Override
            public void onPurchasesUpdated(@NonNull @NotNull BillingResult billingResult, @Nullable @org.jetbrains.annotations.Nullable List<Purchase> list) {
                logResponseCode("onPurchasesUpdated", billingResult.getResponseCode());
            }
        };
    }

    private BillingClientStateListener getClientStateListener() {
        return new BillingClientStateListener() {
            @Override
            public void onBillingServiceDisconnected() {
                Log.e(TAG, "Billing Service Is Disconnected");
            }

            @Override
            public void onBillingSetupFinished(@NonNull @NotNull BillingResult billingResult) {
                logResponseCode("onBillingSetupFinished", billingResult.getResponseCode());
                if(billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    mBillingService.queryPurchases();
                }
            }
        };
    }

    private SkuDetailsResponseListener getSkuResponseListener() {
        return new SkuDetailsResponseListener() {
            @Override
            public void onSkuDetailsResponse(@NonNull @NotNull BillingResult billingResult, @Nullable @org.jetbrains.annotations.Nullable List<SkuDetails> list) {
                logResponseCode("onSkuDetailsResponse", billingResult.getResponseCode());
            }
        };
    }

    private AcknowledgePurchaseResponseListener getAcknowledgeResponseListener() {
        return new AcknowledgePurchaseResponseListener() {
            @Override
            public void onAcknowledgePurchaseResponse(@NonNull @NotNull BillingResult billingResult) {
                logResponseCode("onAcknowledgePurchaseResponse", billingResult.getResponseCode());
            }
        };
    }

    private PurchasesResponseListener getPurchaseResponseListener() {
        return new PurchasesResponseListener() {
            @Override
            public void onQueryPurchasesResponse(@NonNull @NotNull BillingResult billingResult, @NonNull @NotNull List<Purchase> list) {
                logResponseCode("onQueryPurchasesResponse", billingResult.getResponseCode());

                //Disable All Premium Features
                mBillingServiceData.setRemovedAds(false);

                if(list != null && list.size() != 0) {

                    //Enable All Purchased Features

                    for(Purchase purchase: list) {
                        if(purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED) {
                            handlePurchase(purchase);
                            mBillingService.handlePurchase(purchase);
                        }
                    }
                }
            }
        };
    }

    private void handlePurchase(Purchase purchase) {
        String skuId = purchase.getSkus().get(0);

        switch (skuId) {
            case BillingService.REMOVE_ADS:
                mBillingServiceData.setRemovedAds(true);
                break;
        }
    }

    private void logResponseCode(String responseTag, int responseCode) {
        switch (responseCode) {
            case BillingClient.BillingResponseCode.OK:
                Log.e(TAG, responseTag + " -> " + "Billing Response OK");
                break;

            case BillingClient.BillingResponseCode.BILLING_UNAVAILABLE:
                Log.e(TAG, responseTag + " -> " + "Billing Response UNAVAILABLE");
                break;


            case BillingClient.BillingResponseCode.DEVELOPER_ERROR:
                Log.e(TAG, responseTag + " -> " + "Billing Response DEVELOPER_ERROR");
                break;


            case BillingClient.BillingResponseCode.ERROR:
                Log.e(TAG, responseTag + " -> " + "Billing Response ERROR");
                break;


            case BillingClient.BillingResponseCode.FEATURE_NOT_SUPPORTED:
                Log.e(TAG, responseTag + " -> " + "Billing Response FEATURE_NOT_SUPPORTED");
                break;

            case BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED:
                Log.e(TAG, responseTag + " -> " + "Billing Response ITEM_ALREADY_OWNED");
                break;

            case BillingClient.BillingResponseCode.ITEM_NOT_OWNED:
                Log.e(TAG, responseTag + " -> " + "Billing Response ITEM_NOT_OWNED");
                break;

            case BillingClient.BillingResponseCode.ITEM_UNAVAILABLE:
                Log.e(TAG, responseTag + " -> " + "Billing Response ITEM_UNAVAILABLE");
                break;

            case BillingClient.BillingResponseCode.SERVICE_DISCONNECTED:
                Log.e(TAG, responseTag + " -> " + "Billing Response SERVICE_DISCONNECTED");
                break;

            case BillingClient.BillingResponseCode.SERVICE_TIMEOUT:
                Log.e(TAG, responseTag + " -> " + "Billing Response SERVICE_TIMEOUT");
                break;

            case BillingClient.BillingResponseCode.USER_CANCELED:
                Log.e(TAG, responseTag + " -> " + "Billing Response USER_CANCELED");
                break;

            case BillingClient.BillingResponseCode.SERVICE_UNAVAILABLE:
                Log.e(TAG, responseTag + " -> " + "Billing Response SERVICE_UNAVAILABLE");
                break;
        }
    }

    public boolean canShowAds() {
        return !mBillingServiceData.removedAds();
    }
}
