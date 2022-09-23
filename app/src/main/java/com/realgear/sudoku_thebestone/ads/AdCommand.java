package com.realgear.sudoku_thebestone.ads;


import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import org.jetbrains.annotations.NotNull;

public class AdCommand {
    private static final String TOAST_TEXT = "Thanks for Supporting Developer";

    public static final String INTERSTITIAL_AD_UNIT_ID = "ca-app-pub-5091775141119529/8777038210";
    public static final String APP_OPEN_AD_UNIT_ID = "ca-app-pub-5091775141119529/8616977841";
    private final Context mContext;

    public AdCommand(Context mContext) {
        this.mContext = mContext;
    }


    InterstitialAd ad = null;
    public InterstitialAd getInterstitialAd() {
        AdRequest adRequest = new AdRequest.Builder().build();
        ad = null;

        InterstitialAdLoadCallback adLoadCallback = new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull @NotNull InterstitialAd interstitialAd) {
                super.onAdLoaded(interstitialAd);
                ad = interstitialAd;
            }

            @Override
            public void onAdFailedToLoad(@NonNull @NotNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
            }
        };

        InterstitialAd.load(mContext, INTERSTITIAL_AD_UNIT_ID, adRequest, adLoadCallback);


        Toast.makeText(mContext, "Interstitial Ad is loading", Toast.LENGTH_LONG).show();

        return ad;
    }

    //public
}
