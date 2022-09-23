package com.realgear.sudoku_thebestone.utils;

import android.animation.ValueAnimator;
import android.view.View;

import java.util.TreeMap;

public class ValueAnimatorsExtended {
    private final TreeMap<Integer, ValueAnimator> mAnimators;

    public ValueAnimatorsExtended() {
        this.mAnimators = new TreeMap<>();
    }

    public boolean isRunning() {
        boolean result = false;
        for(ValueAnimator animator : mAnimators.values()) {
            if(animator.isRunning()) {
                result = true;
            }
        }

        return result;
    }

    public boolean isStarted() {
        boolean result = false;
        for(ValueAnimator animator : mAnimators.values()) {
            if(animator.isStarted()) {
                result = true;
            }
        }

        return result;
    }

    public boolean isPaused() {
        boolean result = true;
        for(ValueAnimator animator : mAnimators.values()) {
            if(animator.isRunning()) {
                result = false;
            }
        }

        return result;
    }

    public void cancel() {
        for(ValueAnimator animator : mAnimators.values()) {
            if(animator.isRunning()) {
                animator.cancel();
            }

            animator = null;
         }
    }

    public void addAnimator(View view, ValueAnimator valueAnimator) {
        if(mAnimators.containsKey(view.getId())) {
            mAnimators.remove(view.getId());
        }

        mAnimators.put(view.getId(), valueAnimator);
        valueAnimator.start();
    }
}
