package com.realgear.sudoku_thebestone.view;

import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.realgear.sudoku_thebestone.R;
import com.realgear.sudoku_thebestone.adapters.SettingsRecyclerViewAdapter;
import com.realgear.sudoku_thebestone.core.settings.SettingsItem;
import com.realgear.sudoku_thebestone.core.settings.SettingsKey;
import com.realgear.sudoku_thebestone.data.BillingServiceData;
import com.realgear.sudoku_thebestone.data.SettingsData;
import com.realgear.sudoku_thebestone.theme.Themeable;
import com.realgear.sudoku_thebestone.activities.Settings;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;
import java.util.TreeMap;

public class SettingsView extends Themeable {
    private final Settings mActivity;

    private final SettingsData mSettingsData;
    private final TreeMap<SettingsKey, SettingsItem> mItems;
    private final SettingsRecyclerViewAdapter mRecyclerView;
    private final AdView mAdView;
    private Thread mBannerAdThread;

    public SettingsView(Settings activity) {
        this.mActivity = activity;
        this.mItems = new TreeMap<>();
        this.mSettingsData = new SettingsData(mActivity);
        this.mAdView = (AdView)findViewById(R.id.ad_banner_settings);

        initItems();

        if(mSettingsData.isFirstRun()) {
            for(SettingsItem item : mItems.values()) {
                item.setEnabled(true);
            }
            saveSettings();
        }
        else {
            loadSettings();
        }

        this.mRecyclerView = new SettingsRecyclerViewAdapter(new ArrayList<>(mItems.values()));
        initRecyclerView();

        initThread();

        FloatingActionButton backBtn = (FloatingActionButton)findViewById(R.id.back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mActivity.finish();
            }
        });
    }

    private void initRecyclerView() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview_settingsItem);
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        recyclerView.setAdapter(mRecyclerView);
    }

    private void loadSettings() {
        Objects.requireNonNull(mItems.get(SettingsKey.HIGHLIGHT_GOALS)).setEnabled(mSettingsData.getHighlightGoals());
        Objects.requireNonNull(mItems.get(SettingsKey.HIGHLIGHT_REMAINING)).setEnabled(mSettingsData.getHighlightRemaining());
        Objects.requireNonNull(mItems.get(SettingsKey.HIGHLIGHT_SAME_DIGITS)).setEnabled(mSettingsData.getHighlightSameDigits());
        Objects.requireNonNull(mItems.get(SettingsKey.SHOW_TIME)).setEnabled(mSettingsData.getShowTime());
        Objects.requireNonNull(mItems.get(SettingsKey.SHOW_HINT)).setEnabled(mSettingsData.getShowHint());

    }

    public void initItems() {
        mItems.put(SettingsKey.HIGHLIGHT_GOALS,
                new SettingsItem(
                        "Highlight Goals",
                        "Show Solution Values",
                        SettingsKey.HIGHLIGHT_GOALS));

        mItems.put(SettingsKey.HIGHLIGHT_SAME_DIGITS,
                new SettingsItem(
                        "Highlight Same Digits",
                        "Highlight Selected Number",
                        SettingsKey.HIGHLIGHT_SAME_DIGITS));

        mItems.put(SettingsKey.HIGHLIGHT_REMAINING,
                new SettingsItem(
                        "Highlight Remaining",
                        "Highlights Remaining Count of Each Value",
                        SettingsKey.HIGHLIGHT_REMAINING));

        mItems.put(SettingsKey.SHOW_TIME,
                new SettingsItem(
                        "Show Time",
                        "Show The Chronometer",
                        SettingsKey.SHOW_TIME));

        mItems.put(SettingsKey.SHOW_HINT,
                new SettingsItem(
                        "Show Hint",
                        "Enable Hints To Make The Puzzle Less Harder",
                        SettingsKey.SHOW_HINT));

        for(SettingsItem item : mItems.values()) {
            item.setAction(this::saveSettings);
        }
    }

    public void saveSettings() {
        //Log.e("SettingsView", "Saving");
        mSettingsData.setFirstRun();
        mSettingsData.setHighlightGoals(Objects.requireNonNull(mItems.get(SettingsKey.HIGHLIGHT_GOALS)).isEnabled());
        mSettingsData.setHighlightRemaining(Objects.requireNonNull(mItems.get(SettingsKey.HIGHLIGHT_REMAINING)).isEnabled());
        mSettingsData.setHighlightSameDigits(Objects.requireNonNull(mItems.get(SettingsKey.HIGHLIGHT_SAME_DIGITS)).isEnabled());
        mSettingsData.setShowHint(Objects.requireNonNull(mItems.get(SettingsKey.SHOW_HINT)).isEnabled());
        mSettingsData.setShowTime(Objects.requireNonNull(mItems.get(SettingsKey.SHOW_TIME)).isEnabled());
    }

    private void initThread() {
        mBannerAdThread = new Thread(this::setAdState);
        mBannerAdThread.setName("Banner_Ad");
        mBannerAdThread.run();
    }

    public void setAdState() {
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(@NonNull @NotNull LoadAdError loadAdError) {
                initBannerAd();
            }

            @Override
            public void onAdLoaded() {
                Log.e("Ad Loader", "Ad Loaded Succesfully");
            }
        });

        BillingServiceData data = new BillingServiceData(mActivity);

        if(!data.removedAds()) {
            initBannerAd();
        }
    }

    public void initBannerAd() {
        try {
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);
        }
        catch (Exception e) {
            Log.e("Ad Loader", e.toString());
        }
    }

    public View findViewById(int id) {
        return mActivity.findViewById(id);
    }
}
