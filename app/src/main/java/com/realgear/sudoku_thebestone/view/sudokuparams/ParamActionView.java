package com.realgear.sudoku_thebestone.view.sudokuparams;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.realgear.sudoku_thebestone.theme.Theme;
import com.realgear.sudoku_thebestone.theme.Themeable;
import com.realgear.sudoku_thebestone.utils.Action;

public class ParamActionView extends Themeable {
    public static final String TAG = ParamActionView.class.getSimpleName();

    private Theme mTheme;

    private final Button mButton;
    private final FrameLayout mFrame;
    private final ImageView mIcon;
    private final ParamActionEnum mAction;
    private final ParamActionTypeEnum mType;

    private ParamEnum mState;
    private boolean isChecked;


    public ParamActionView(Button button, FrameLayout frameLayout, ImageView icon, ParamActionEnum action, ParamActionTypeEnum type) {
        this.mButton = button;
        this.mFrame = frameLayout;
        this.mIcon = icon;
        this.mAction = action;
        this.mType = type;

        isChecked = false;
        initParam();
    }

    @SuppressLint("ClickableViewAccessibility")
    public void initParam() {
        if(mType == ParamActionTypeEnum.NORMAL) {
            onTouchListener(mButton,
                    () -> {
                        mFrame.setBackgroundTintList(
                                ColorStateList.valueOf(mTheme.getPrimaryColor()));
                        mButton.setTextColor(Color.WHITE);
                        mIcon.setImageTintList(
                                ColorStateList.valueOf(Color.WHITE));
                    },
                    () -> {
                        mFrame.setBackgroundTintList(
                                ColorStateList.valueOf(Color.argb(0, 0, 0, 0)));
                        mButton.setTextColor(mTheme.getPrimaryColor());
                        mIcon.setImageTintList(
                                ColorStateList.valueOf(mTheme.getPrimaryColor()));
                    });
        }
        else {
            onTouchListener_2(mButton,
                    () -> {
                        onTouchEventsThemes(isChecked);
                    },
                    () -> {
                        onTouchEventsThemes(!isChecked);
                    },
                    () -> {
                        isChecked = !isChecked;
                    });
        }
    }

    public void onTouchEventsThemes(boolean invert) {
        if(invert) {
            mFrame.setBackgroundTintList(
                    ColorStateList.valueOf(Color.argb(0, 0, 0, 0)));
            mButton.setTextColor(mTheme.getPrimaryColor());
            mIcon.setImageTintList(
                    ColorStateList.valueOf(mTheme.getPrimaryColor()));
        }
        else {
            mFrame.setBackgroundTintList(
                    ColorStateList.valueOf(mTheme.getPrimaryColor()));
            mButton.setTextColor(Color.WHITE);
            mIcon.setImageTintList(
                    ColorStateList.valueOf(Color.WHITE));
        }
    }

    public void setState(ParamEnum state) {
        this.mState = state;

        switch (mState) {
            case ICON_ONLY:
                showIcon();
                break;
            case TEXT_ONLY:
                showText();
                break;
        }
    }

    public void showIcon() {
        this.mIcon.setVisibility(View.VISIBLE);
        this.mButton.setText("");
    }

    public void showText() {
        this.mIcon.setVisibility(View.INVISIBLE);
        this.mButton.setText(mAction.toString());
    }

    @Override
    public void updateTheme() {
        //Change Text, Icon, Border, Background Icon
        mTheme = getTheme();

        mButton.setBackgroundTintList(ColorStateList.valueOf(mTheme.getPrimaryColor()));
        mButton.setTextColor((isChecked) ? Color.WHITE : mTheme.getPrimaryColor());
        if(isChecked) {
            mFrame.setBackgroundTintList(ColorStateList.valueOf(mTheme.getPrimaryColor()));
        }
        mFrame.setBackgroundTintList((isChecked) ? ColorStateList.valueOf(mTheme.getPrimaryColor())
                : ColorStateList.valueOf(Color.WHITE));
        mIcon.setImageTintList(ColorStateList.valueOf(mTheme.getPrimaryColor()));
    }

    @SuppressLint("ClickableViewAccessibility")
    private void onTouchListener_2(Button button, Action onTouchDownAction, Action onTouchCancelAction, Action onTouchUpAction) {
        button.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                try {
                    switch (motionEvent.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                        case MotionEvent.ACTION_HOVER_ENTER:
                            onTouchDownAction.call();
                            break;
                        case MotionEvent.ACTION_CANCEL:
                        case MotionEvent.ACTION_HOVER_EXIT:
                            onTouchCancelAction.call();
                            break;
                        case MotionEvent.ACTION_UP:
                            onTouchUpAction.call();
                            break;
                    }
                }
                catch (Exception e) {

                }

                return false;
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void onTouchListener(Button button, Action onTouchDownAction, Action onTouchUpAction) {
        if(button == null) {
            return;
        }

        button.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                try {
                    switch (motionEvent.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                        case MotionEvent.ACTION_HOVER_ENTER:
                            onTouchDownAction.call();
                            break;
                        case MotionEvent.ACTION_UP:
                        case MotionEvent.ACTION_CANCEL:
                        case MotionEvent.ACTION_HOVER_EXIT:
                            onTouchUpAction.call();
                            break;

                        default:
                            return false;

                    }
                }
                catch (Exception e) {

                }
                return false;
            }
        });
    }

    public Button getButton() {
        return this.mButton;
    }

    public boolean isChecked() {
        return this.isChecked;
    }

    public void setVisibility(int visibility) {
        this.mFrame.setVisibility(visibility);
    }

    public void unCheck() {
        isChecked = false;
        updateTheme();
    }
}
