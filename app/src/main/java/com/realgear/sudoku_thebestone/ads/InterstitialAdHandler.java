package com.realgear.sudoku_thebestone.ads;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.realgear.sudoku_thebestone.data.BillingServiceData;
import com.realgear.sudoku_thebestone.utils.Action;

import org.jetbrains.annotations.NotNull;

public class InterstitialAdHandler {
    public static final String INTERSTITIAL_AD_UNIT_ID = "ca-app-pub-5091775141119529/8777038210";
    private static final String TAG = InterstitialAdHandler.class.getSimpleName();

    private final Activity mActivity;

    private InterstitialAd mAd;

    private InterstitialAdLoadCallback mCallback;

    private boolean adShowed = false;

    BillingServiceData data;

    public InterstitialAdHandler(Activity activity) {
        this.mActivity = activity;

        data = new BillingServiceData(activity);

        start();
    }

    public void start() {
        mCallback = new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull @NotNull InterstitialAd interstitialAd) {
                super.onAdLoaded(interstitialAd);
                mAd = interstitialAd;
                //showAd();
            }

            @Override
            public void onAdFailedToLoad(@NonNull @NotNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                load();
            }
        };
        load();
    }

    private void load() {
        if(data.removedAds())
            return;

        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(mActivity, INTERSTITIAL_AD_UNIT_ID, adRequest, mCallback);
    }

    public void showAd(Action action) throws Exception {
        if(mAd != null) {
            mAd.show(mActivity);
            mAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdFailedToShowFullScreenContent(@NonNull @NotNull AdError adError) {
                    super.onAdFailedToShowFullScreenContent(adError);
                    Log.e(TAG, "onAdFailedToShowFullScreenContent");
                    load();
                    try {
                        action.call();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onAdShowedFullScreenContent() {
                    super.onAdShowedFullScreenContent();
                    Log.e(TAG, "onAdShowedFullScreenContent");
                    adShowed = true;
                }

                @Override
                public void onAdDismissedFullScreenContent() {
                    super.onAdDismissedFullScreenContent();
                    load();
                    try {
                        action.call();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onAdImpression() {
                    super.onAdImpression();
                    Log.e(TAG, "onAdImpression");
                }
            });
        }
        else {
            action.call();
        }
    }

}
