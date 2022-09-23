package com.realgear.sudoku_thebestone.ads;

public class AdType {
    public AdEnum mAdType;
    public Object mAd;
    public boolean isLoaded;
    public boolean isFailed;

    public AdType(AdEnum adType) {
        this.mAdType = adType;
    }
}
