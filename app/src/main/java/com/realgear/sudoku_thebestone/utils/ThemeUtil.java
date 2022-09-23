package com.realgear.sudoku_thebestone.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ThemeUtil {

    @SuppressLint("ClickableViewAccessibility")
    public static void setOnTouchListener(Button button, Action onTouchDownAction, Action onTouchUpAction) {
        if(button == null)
            return;

        button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                try {
                    switch (motionEvent.getAction()) {
                        case MotionEvent.ACTION_UP:
                        case MotionEvent.ACTION_HOVER_ENTER:
                            onTouchDownAction.call();
                            break;

                        case MotionEvent.ACTION_POINTER_UP:
                        case MotionEvent.ACTION_CANCEL:
                        case MotionEvent.ACTION_HOVER_EXIT:
                            onTouchUpAction.call();
                            break;

                        default:
                            return false;
                    }
                }
                catch (Exception ignored) {}
                return false;
            }
        });
    }

    public static void addListener(Object button, Action action) {
        if(button == null)
            return;

        try {
            FloatingActionButton btn = (FloatingActionButton)button;
            btn.setOnClickListener(view -> {
                try {
                    action.call();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            try {
                Button btn = (Button) button;
                btn.setOnClickListener(view -> {
                    try {
                        action.call();
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                });
            }
            catch (Exception ignored) { }
        }
    }

    public static void setStatusBarColor(Activity activity, Integer color, boolean darkStatusBarTint) {
        Window window = activity.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(color);
        int flag = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        window.getDecorView().setSystemUiVisibility(darkStatusBarTint ? flag : 0);
    }
}
