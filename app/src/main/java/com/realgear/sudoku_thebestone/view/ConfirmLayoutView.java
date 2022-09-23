package com.realgear.sudoku_thebestone.view;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.realgear.sudoku_thebestone.R;
import com.realgear.sudoku_thebestone.theme.Theme;
import com.realgear.sudoku_thebestone.utils.Action;

public class ConfirmLayoutView {
    private LayoutInflater mLayoutInflater;
    private View mParent;
    private View mView;

    private Action mAction;
    private Theme mTheme;
    private String mActionName;

    private View mTextViewParent;

    private Button mBtn_Cancel;
    private Button mBtn_Confirm;


    public ConfirmLayoutView(
            LayoutInflater layoutInflater,
            View parent,
            Action action,
            String actionName,
            Theme theme) {
        this.mLayoutInflater = layoutInflater;
        this.mParent = parent;
        this.mAction = action;
        this.mActionName = actionName;
        this.mTheme = theme;

        mView = mLayoutInflater.inflate(R.layout.content_confirm_layout, (ViewGroup) parent, false);

        ((ViewGroup) parent).addView(mView);
        mView.setAlpha(0.0f);
        mTextViewParent = (View)mView.findViewById(R.id.textView_parent);

        initUI();
    }

    private void initUI() {
        FrameLayout frameCancel = (FrameLayout)mView.findViewById(R.id.frame_cancel);
        FrameLayout frameConfirm = (FrameLayout)mView.findViewById(R.id.frame_confirm);

        mBtn_Cancel = (Button)mView.findViewById(R.id.btn_cancel);
        mBtn_Confirm = (Button)mView.findViewById(R.id.btn_confirm);

        onTouchListener(mBtn_Cancel,
                () -> {
                    frameCancel.setBackgroundTintList(ColorStateList.valueOf(mTheme.getPrimaryColor()));
                    mBtn_Cancel.setTextColor(Color.WHITE);
                },
                () -> {
                    frameCancel.setBackgroundTintList(ColorStateList.valueOf(Color.argb(0, 0, 0, 0)));
                    mBtn_Cancel.setTextColor(mTheme.getPrimaryColor());
                });

        onTouchListener(mBtn_Confirm,
                () -> {
                    frameConfirm.setBackgroundTintList(ColorStateList.valueOf(mTheme.getPrimaryColor()));
                    mBtn_Confirm.setTextColor(Color.WHITE);
                },
                () -> {
                    frameConfirm.setBackgroundTintList(ColorStateList.valueOf(Color.argb(0, 0, 0, 0)));
                    mBtn_Confirm.setTextColor(mTheme.getPrimaryColor());
                });

        mBtn_Confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("CLV", "Confirm Clicked");
                close();
                try {
                    mAction.call();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        mBtn_Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("CLV", "Cancel Clicked");
                close();
            }
        });

        mBtn_Confirm.setBackgroundTintList(ColorStateList.valueOf(mTheme.getPrimaryColor()));
        mBtn_Confirm.setTextColor(mTheme.getPrimaryColor());

        mBtn_Cancel.setBackgroundTintList(ColorStateList.valueOf(mTheme.getPrimaryColor()));
        mBtn_Cancel.setTextColor(mTheme.getPrimaryColor());

        mBtn_Confirm.setText(mActionName);
    }

    public void addText(TextView textView) {
        textView.setTextColor(mTheme.getPrimaryColor());
        ((ViewGroup)mTextViewParent).addView(textView);
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

    private void close() {
        ((ViewGroup)mParent).removeView(mView);
        ((ViewGroup)mParent).forceLayout();
    }

    public void show() {
        mView.setAlpha(1.0f);
    }

}
