package com.realgear.sudoku_thebestone.utils;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.widget.Button;
import android.widget.FrameLayout;

import com.realgear.sudoku_thebestone.theme.Themeable;

public class MaterialButton extends Themeable {
    private final Activity      mActivity;

    private final FrameLayout   mFrameLayout;
    private final Button        mButton;

    public MaterialButton(Activity activity, int frameLayoutId, int buttonId) {
        this.mActivity      = activity;
        this.mFrameLayout   = (FrameLayout) this.mActivity.findViewById(frameLayoutId);
        this.mButton        = (Button) this.mActivity.findViewById(buttonId);

        initUI();
    }

    private void initUI() {
        if(mFrameLayout == null || mButton == null)
            return;

        ThemeUtil.setOnTouchListener(mButton,
            () -> {
                mFrameLayout.setBackgroundTintList(ColorStateList.valueOf(getTheme().getPrimaryColor()));
                mButton.setTextColor(Color.WHITE);
            },
            () -> {
                mFrameLayout.setBackgroundTintList(ColorStateList.valueOf(Color.argb(0, 0, 0,0)));
                mButton.setTextColor(getTheme().getPrimaryColor());
            }
        );
    }

    public void setText(String text) {
        this.mButton.setText(text);
    }
}
