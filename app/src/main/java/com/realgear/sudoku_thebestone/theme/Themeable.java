package com.realgear.sudoku_thebestone.theme;

import android.animation.ArgbEvaluator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.view.View;

import java.util.TreeMap;

public class Themeable {
    private Theme mTheme;
    private Theme mPrevTheme;
    private Theme mCurTheme;

    private final int ANIM_DURATION = 250;

    public Themeable() {

    }

    public void setTheme(Theme theme) {

        if(this.mTheme == null) {
            this.mTheme = theme;
            this.mPrevTheme = theme;
            this.mCurTheme = new Theme();

        }
        else {
            mPrevTheme = mTheme;
            mTheme = theme;
        }

        ValueAnimator primaryColor = getColorAnimator(this.mPrevTheme.getPrimaryColor(), mTheme.getPrimaryColor());
        ValueAnimator secondaryColor = getColorAnimator(this.mPrevTheme.getSecondaryColor(), mTheme.getSecondaryColor());
        ValueAnimator secondaryColor2 = getColorAnimator(this.mPrevTheme.getSecondaryColor2(), mTheme.getSecondaryColor2());
        ValueAnimator backgroundColor = getColorAnimator(this.mPrevTheme.getBackgroundColor(), mTheme.getBackgroundColor());

        primaryColor.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mCurTheme.setPrimaryColor((int) valueAnimator.getAnimatedValue());
                updateTheme();
            }
        });
        secondaryColor.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mCurTheme.setSecondaryColor((int) valueAnimator.getAnimatedValue());
                updateTheme();
            }
        });
        secondaryColor2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mCurTheme.setSecondaryColor2((int) valueAnimator.getAnimatedValue());
                updateTheme();
            }
        });
        backgroundColor.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mCurTheme.setBackgroundColor((int) valueAnimator.getAnimatedValue());
                updateTheme();
            }
        });

        this.mCurTheme.setOrdinal(theme.getOrdinal());

        primaryColor.start();
        secondaryColor.start();
        secondaryColor2.start();
        backgroundColor.start();
    }

    private ValueAnimator getColorAnimator(int colorFrom, int colorTo) {
        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        colorAnimation.setDuration(ANIM_DURATION);
        return colorAnimation;
    }

    TreeMap<Integer, ValueAnimator> mAnimators = new TreeMap<>();

    public void startFading(View view, float from, float to, int duration, boolean gone) {
        if(mAnimators.containsKey(view.getId())) {
            ValueAnimator oldAnimator = mAnimators.get(view.getId());
            oldAnimator.cancel();

            float curVal = (float)oldAnimator.getAnimatedValue();
            ValueAnimator newAnimator = ValueAnimator.ofObject(new TypeEvaluator<Float>() {
                @Override
                public Float evaluate(float fraction, Float startValue, Float endValue) {
                    return (startValue + (endValue - startValue) * fraction);
                }
            }, curVal, to);
            newAnimator.setDuration(duration);
            mAnimators.remove(view.getId(), oldAnimator);

            newAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    float value = (float) valueAnimator.getAnimatedValue();

                    view.setAlpha(value);

                    if (from == 0.0f) {
                        view.setVisibility(View.VISIBLE);
                    } else if (from == 1.0f && value == 0.0f) {
                        view.setVisibility(gone ? View.GONE : View.INVISIBLE);
                    }
                }
            });

            mAnimators.put(view.getId(), newAnimator);
            newAnimator.start();
        }
        else {
            ValueAnimator floatAnimation = ValueAnimator.ofObject(new TypeEvaluator<Float>() {
                @Override
                public Float evaluate(float fraction, Float startValue, Float endValue) {
                    return (startValue + (endValue - startValue) * fraction);
                }
            }, from, to);
            floatAnimation.setDuration(duration);
            floatAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    float value = (float) valueAnimator.getAnimatedValue();
                    view.setAlpha(value);
                    if (from == 0.0f) {
                        view.setVisibility(View.VISIBLE);
                    } else if (from == 1.0f && value == 0.0f) {
                        view.setVisibility(gone ? View.GONE : View.INVISIBLE);
                    }
                }
            });

            floatAnimation.start();

            mAnimators.put(view.getId(), floatAnimation);

        }
    }

    public boolean isAnimatorRunning(View view) {
        if(mAnimators.containsKey(view.getId())) {
            return mAnimators.get(view.getId()).isRunning();
        }
        return false;
    }

    public Theme getTheme() {
        return this.mCurTheme;
    }

    public void updateTheme() {

    }
}
