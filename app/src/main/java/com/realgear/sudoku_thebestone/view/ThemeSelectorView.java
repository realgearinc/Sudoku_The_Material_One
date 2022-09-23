package com.realgear.sudoku_thebestone.view;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.realgear.sudoku_thebestone.R;
import com.realgear.sudoku_thebestone.theme.ThemeManager;
import com.realgear.sudoku_thebestone.theme.Themeable;
import com.realgear.sudoku_thebestone.theme.Themes;
import com.realgear.sudoku_thebestone.utils.Action;

public class ThemeSelectorView {

    private FloatingActionButton mFab_Main, mFab_Color_1, mFab_Color_2, mFab_Color_3, mFab_Color_4, mFab_Color_5;
    private Animation mFab_Open, mFab_Close, mFab_Clock, mFab_Anticlock;

    private boolean isOpened = false;

    public Themes       mCurrentTheme;
    public Themeable    mThemeable;
    public ThemeManager mThemeManager;

    public ThemeSelectorView(Context ctx, View view, Themeable mThemeable) {
        mFab_Main    = view.findViewById(R.id.btn_menu);
        mFab_Color_1 = view.findViewById(R.id.btn_color_1);
        mFab_Color_2 = view.findViewById(R.id.btn_color_2);
        mFab_Color_3 = view.findViewById(R.id.btn_color_3);
        mFab_Color_4 = view.findViewById(R.id.btn_color_4);
        mFab_Color_5 = view.findViewById(R.id.btn_color_5);

        mFab_Open       = AnimationUtils.loadAnimation(ctx, R.anim.fab_open);
        mFab_Close      = AnimationUtils.loadAnimation(ctx, R.anim.fab_close);
        mFab_Clock      = AnimationUtils.loadAnimation(ctx, R.anim.fab_rotate_clock);
        mFab_Anticlock  = AnimationUtils.loadAnimation(ctx, R.anim.fab_rotate_anticlock);

        mFab_Main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isOpened) {
                    closeMenu();
                }
                else {
                    openMenu();
                }
            }
        });

        this.mThemeable = mThemeable;
        this.mThemeManager = new ThemeManager(ctx);

        addListener(mFab_Color_1, () -> selectTheme(0));
        addListener(mFab_Color_2, () -> selectTheme(1));
        addListener(mFab_Color_3, () -> selectTheme(2));
        addListener(mFab_Color_4, () -> selectTheme(3));
        addListener(mFab_Color_5, () -> selectTheme(4));

        closeMenu();
    }

    public void closeMenu() {
        startAnimation(mFab_Color_1, mFab_Close, false);
        startAnimation(mFab_Color_2, mFab_Close, false);
        startAnimation(mFab_Color_3, mFab_Close, false);
        startAnimation(mFab_Color_4, mFab_Close, false);
        startAnimation(mFab_Color_5, mFab_Close, false);

        startAnimation(mFab_Main, mFab_Anticlock, true);
        isOpened = false;
    }

    public void openMenu() {
        startAnimation(mFab_Color_1, mFab_Open, true);
        startAnimation(mFab_Color_2, mFab_Open, true);
        startAnimation(mFab_Color_3, mFab_Open, true);
        startAnimation(mFab_Color_4, mFab_Open, true);
        startAnimation(mFab_Color_5, mFab_Open, true);

        startAnimation(mFab_Main, mFab_Clock, true);
        isOpened = true;
    }

    public void selectTheme(Themes theme) {
        mCurrentTheme = theme;
        mThemeable.setTheme(mThemeManager.getTheme(theme));
    }

    public int getCurrentTheme() {
        return this.mCurrentTheme.ordinal();
    }

    public void selectTheme(Integer ordinal) {
        Themes theme = Themes.values()[ordinal];
        selectTheme(theme);
    }

    public void addListener(Object button, Action action) {
        if(button == null)
            return;

        try {
            FloatingActionButton btn = (FloatingActionButton)button;
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        action.call();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            try {
                Button btn = (Button) button;
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            action.call();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            } catch (Exception x) {}
        }
    }

    public void startAnimation(FloatingActionButton btn, Animation anim, boolean isClickable) {
        btn.startAnimation(anim);
        btn.setClickable(isClickable);
    }
}
