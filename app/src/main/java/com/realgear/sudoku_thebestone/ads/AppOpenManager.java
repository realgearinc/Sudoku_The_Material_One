package com.realgear.sudoku_thebestone.ads;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.appopen.AppOpenAd;
import com.realgear.sudoku_thebestone.data.BillingServiceData;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

import org.jetbrains.annotations.NotNull;

public class AppOpenManager implements Application.ActivityLifecycleCallbacks, LifecycleObserver {
    public static final String TAG = AppOpenManager.class.getSimpleName();


    private AppOpenAd mAppOpenAd = null;

    private AppOpenAd.AppOpenAdLoadCallback loadCallback;
    private Activity mCurrentActivity;
    private static boolean isShowingAd = false;

    private final Application myApplication;

    BillingServiceData data;

    public AppOpenManager(Application myApplication) {
        this.myApplication = myApplication;

        this.data = new BillingServiceData(myApplication);

        if(!data.removedAds()) {
            this.myApplication.registerActivityLifecycleCallbacks(this);
            ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
        }
    }
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onStart() {
        showAdIfAvailable();
        Log.e(TAG, "OnStart Should Show Ad");
    }

    public void fetchAd() {
        if(isAvailable()) {
            return;
        }

        loadCallback = new AppOpenAd.AppOpenAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull @NotNull AppOpenAd appOpenAd) {
                mAppOpenAd = appOpenAd;
            }

            @Override
            public void onAdFailedToLoad(@NonNull @NotNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
            }
        };
        AdRequest request = getAdRequest();
        AppOpenAd.load(myApplication, AdCommand.APP_OPEN_AD_UNIT_ID, request,
                AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT, loadCallback);
    }

    private AdRequest getAdRequest() {
        return new AdRequest.Builder().build();
    }

    public boolean isAvailable() {
        return mAppOpenAd != null;
    }

    public void showAdIfAvailable() {
        if(!isShowingAd && !isAvailable()) {
            fetchAd();
        }

        if(!isShowingAd && isAvailable()) {
            Log.e(TAG, "Will Show Ad");

            FullScreenContentCallback fullScreenContentCallback = new FullScreenContentCallback() {
                @Override
                public void onAdFailedToShowFullScreenContent(@NonNull @NotNull AdError adError) {
                    super.onAdFailedToShowFullScreenContent(adError);
                }

                @Override
                public void onAdShowedFullScreenContent() {
                    isShowingAd = true;
                    Log.e(TAG, "Ädd is Showing");
                }

                @Override
                public void onAdDismissedFullScreenContent() {
                    mAppOpenAd = null;
                    isShowingAd = false;
                    fetchAd();
                }

                @Override
                public void onAdImpression() {
                    super.onAdImpression();
                }
            };

            mAppOpenAd.setFullScreenContentCallback(fullScreenContentCallback);
            mAppOpenAd.show(mCurrentActivity);
        } else {
          Log.e(TAG, "Can not show");
          fetchAd();
        }
    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle bundle) {
        mCurrentActivity = activity;
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {
        mCurrentActivity = activity;
    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {

    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {

    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle bundle) {

    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
        mCurrentActivity = null;
    }
}
